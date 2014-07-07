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

import com.google.common.base.Function;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.EnrichmentTest;
import org.gitools.analysis.stats.test.ZscoreTest;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.analysis.stats.test.results.SimpleResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.IdentityMatrixFunction;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.model.matrix.element.MapLayerAdapter;
import org.gitools.utils.cutoffcmp.CutoffMatrixFunction;

import java.util.*;
import java.util.concurrent.CancellationException;

import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.intersection;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class EnrichmentProcessor implements AnalysisProcessor {

    private final EnrichmentAnalysis analysis;

    public EnrichmentProcessor(EnrichmentAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(final IProgressMonitor monitor) {
        Date startTime = new Date();

        IMatrix data = analysis.getData().get();
        final IMatrixLayer<Double> layer = data.getLayers().get(analysis.getLayer());

        final TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());
        final ThreadLocal<EnrichmentTest> test = new ThreadLocal<EnrichmentTest>() {
            @Override
            protected EnrichmentTest initialValue() {
                return testFactory.create();
            }
        };

        final LayerAdapter<SimpleResult> adapter = new LayerAdapter<>(test.get().getResultClass());
        final IModuleMap moduleMap = analysis.getModuleMap().get();
        final IMatrixDimension conditions = data.getColumns();
        final IMatrixDimension items = data.getRows();
        final IMatrixDimension modules = new HashMatrixDimension(ROWS, moduleMap.getModules());

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                modules,
                conditions
        );

        //TODO Improve enrichment wizard and rewrite EnrichmentAnalysis.
        // The cutoff is a property of the test and not an analysis property.
        final IMatrixFunction<Double, Double> cutoffFunction;
        if (analysis.isBinaryCutoffEnabled() && !ZscoreTest.class.isAssignableFrom(test.getClass())) {
            cutoffFunction = new CutoffMatrixFunction(analysis.getBinaryCutoffCmp(), analysis.getBinaryCutoffValue());
        } else {
            cutoffFunction = new IdentityMatrixFunction<>();
        }

        // Detect the list of items presents in the population and not in the data matrix
        final Set<String> missingBackgroundItems = new HashSet<>();
        final Function<Object, Double> backgroundValue = constant(analysis.getPopulationDefaultValue());

        if (analysis.getPopulation() != null) {

            Set<String> background = analysis.getPopulation().get();

            if (analysis.isDiscardNonMappedRows()) {
                background = intersection(background, moduleMap.getItems());
            }

            missingBackgroundItems.addAll(background);

            for (String item : items) {
                missingBackgroundItems.remove(item);
            }
        }

        // Run enrichment
        data.newPosition().iterate(layer, conditions)
                .monitor(monitor, "Running enrichment analysis")
                .transform(new AbstractMatrixFunction<Map<String, SimpleResult>, Double>() {

                    @Override
                    public Map<String, SimpleResult> apply(Double value, IMatrixPosition position) {

                        IMatrixIterable<Double> population;

                        // Discard not mapped items
                        if (analysis.isDiscardNonMappedRows()) {
                            population = position.iterate(layer, items.subset(moduleMap.getItems()));
                        } else {
                            population = position.iterate(layer, items);
                        }

                        // Apply cutoff
                        population = population.transform(cutoffFunction);

                        test.get().processPopulation(
                                concat(
                                        population,
                                        transform(missingBackgroundItems, backgroundValue)
                                )
                        );

                        Map<String, SimpleResult> results = new HashMap<>();
                        for (String module : moduleMap.getModules()) {

                            if (monitor.isCancelled()) {
                                throw new CancellationException();
                            }

                            Set<String> moduleItems = moduleMap.getMappingItems(module);

                            Iterable<Double> moduleValues = position
                                    .iterate(layer, items.subset(moduleItems))
                                    .transform(cutoffFunction);

                            if (!missingBackgroundItems.isEmpty()) {

                                moduleValues = concat(
                                        moduleValues,
                                        transform(
                                                intersection(missingBackgroundItems, moduleItems),
                                                backgroundValue
                                        )
                                );

                            }

                            SimpleResult result = test.get().processTest(moduleValues);
                            if (result != null && result.getN() >= analysis.getMinModuleSize() && result.getN() <= analysis.getMaxModuleSize()) {
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

    }
}
