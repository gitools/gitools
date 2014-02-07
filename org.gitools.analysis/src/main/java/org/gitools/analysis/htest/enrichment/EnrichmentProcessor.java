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
package org.gitools.analysis.htest.enrichment;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.Test;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixFunction;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.AbstractMatrixFunction;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.model.matrix.element.MapLayerAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EnrichmentProcessor implements AnalysisProcessor {

    private final EnrichmentAnalysis analysis;

    public EnrichmentProcessor(EnrichmentAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {
        Date startTime = new Date();

        IMatrix data = analysis.getData().get();
        final IMatrixLayer<Double> layer = data.getLayers().iterator().next();
        final Test test = TestFactory.createFactory(analysis.getTestConfig()).create();
        final LayerAdapter<CommonResult> adapter = new LayerAdapter<>(test.getResultClass());
        final IModuleMap moduleMap = analysis.getModuleMap().get();
        final IMatrixDimension conditions = data.getColumns();
        final IMatrixDimension items = data.getRows();
        final IMatrixDimension modules = new HashMatrixDimension(ROWS, moduleMap.getModules());

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                modules,
                conditions
        );

        // Run enrichment
        data.newPosition().iterate(layer, conditions)
                .monitor(monitor, "Running enrichment analysis")
                .transform(new AbstractMatrixFunction<Map<String, CommonResult>, Double>() {

                    @Override
                    public Map<String, CommonResult> apply(Double value, IMatrixPosition position) {

                        IMatrixIterable<Double> population = position.iterate(layer, items);
                        if (analysis.isDiscardNonMappedRows()) {
                            population = population.filter(moduleMap.getItems());
                        }

                        test.processPopulation(population);

                        Map<String, CommonResult> results = new HashMap<>();
                        for (String module : moduleMap.getModules()) {

                            Iterable<Double> moduleValues = position.iterate(layer, items).filter(moduleMap.getMappingItems(module));

                            CommonResult result = test.processTest(moduleValues);
                            if (result.getN() >= analysis.getMinModuleSize() && result.getN() <= analysis.getMaxModuleSize()) {
                                results.put(module, result);
                            }
                        }

                        return results;
                    }
                })
                .store(resultsMatrix, new MapLayerAdapter<>(modules, adapter));

        // Run multiple test correction
        IMatrixFunction<Double, Double> mtcFunction = MTCFactory.createFunction(analysis.getMtc());
        IMatrixPosition position = resultsMatrix.newPosition();
        for (String condition : position.iterate(conditions)) {

            // Left p-Value
            position.iterate(adapter.getLayer(Double.class, "left-p-value"), modules)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-left-p-value"));

            // Right p-Value
            position.iterate(adapter.getLayer(Double.class, "right-p-value"), modules)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-right-p-value"));

            // Two-tail p-Value
            position.iterate(adapter.getLayer(Double.class, "two-tail-p-value"), modules)
                    .transform(mtcFunction)
                    .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-two-tail-p-value"));

        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultsMatrix));

        monitor.end();
    }
}
