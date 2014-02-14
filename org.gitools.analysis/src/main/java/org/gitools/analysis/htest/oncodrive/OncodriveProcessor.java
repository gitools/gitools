/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.analysis.htest.oncodrive;

import com.google.common.collect.Iterables;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.Test;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.AbstractMatrixFunction;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.PositionMapping;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.modulemap.HashModuleMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;

public class OncodriveProcessor implements AnalysisProcessor {

    private final OncodriveAnalysis analysis;

    public OncodriveProcessor(OncodriveAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {
        Date startTime = new Date();

        IMatrix data = analysis.getData().get();
        final IMatrixLayer<Double> layer = data.getLayers().iterator().next();
        final Test test = TestFactory.createFactory(analysis.getTestConfig()).create();
        final LayerAdapter<CommonResult> adapter = new LayerAdapter<>(test.getResultClass());
        final IModuleMap moduleMap = (analysis.getModuleMap().get() == null ? new HashModuleMap().addMapping("All data columns", data.getColumns()) : analysis.getModuleMap().get());
        final IMatrixDimension genes = data.getRows();
        final IMatrixDimension samples = data.getColumns();
        final IMatrixDimension sampleModules = new HashMatrixDimension(COLUMNS, moduleMap.getModules());

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                genes,
                sampleModules
        );

        // Start Oncodrive analysis
        for (final String module : sampleModules) {

            // Calculate population (all the values of all the genes and the samples of the current module)
            List<Double> population = new ArrayList<>();
            IMatrixPosition position = data.newPosition();
            for (String sample : moduleMap.getMappingItems(module)) {
                Iterables.addAll(population, position.set(samples, sample).iterate(layer, genes));
            }
            test.processPopulation(population);

            // Module samples
            final Set<String> moduleSamples = moduleMap.getMappingItems(module);

            // Skip unwanted modules
            if (moduleSamples.size() < analysis.getMinModuleSize() || moduleSamples.size() > analysis.getMaxModuleSize()) {
                continue;
            }

            // Iterate all the genes
            data.newPosition().iterate(layer, genes)
                    .monitor(monitor, "Running oncodrive analysis module '" + module + "'")
                    .transform(new AbstractMatrixFunction<CommonResult, Double>() {

                        @Override
                        public CommonResult apply(Double value, IMatrixPosition position) {

                            // Values of the current module
                            Iterable<Double> moduleValues = position.iterate(layer, samples).filter(moduleSamples);

                            // Execute the test
                            return test.processTest(moduleValues);
                        }
                    })
                    .store(resultsMatrix, new PositionMapping().map(genes, genes).fix(sampleModules, module), adapter);

        }

        // Run multiple test correction
        IMatrixFunction<Double, Double> mtcFunction = MTCFactory.createFunction(analysis.getMtc());
        IMatrixPosition position = resultsMatrix.newPosition();
        for (String modules : position.iterate(sampleModules).monitor(monitor, "Running multiple test correction")) {

            // Left p-Value
            position.iterate(adapter.getLayer(Double.class, "left-p-value"), genes)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-left-p-value"));

            // Right p-Value
            position.iterate(adapter.getLayer(Double.class, "right-p-value"), genes)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-right-p-value"));

            // Two-tail p-Value
            position.iterate(adapter.getLayer(Double.class, "two-tail-p-value"), genes)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-two-tail-p-value"));

        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultsMatrix));

        monitor.end();
    }
}
