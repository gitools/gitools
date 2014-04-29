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
package org.gitools.analysis.mutualexclusive;

import com.google.common.primitives.Doubles;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.stats.test.MutualExclusiveTest;
import org.gitools.analysis.stats.test.results.SimpleResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;

import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.filter.NotNullPredicate;
import org.gitools.matrix.filter.ValueFilterFunction;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.sort.mutualexclusion.AggregationFunction;
import org.gitools.utils.aggregation.CutoffCmpCountAggregator;


import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import com.google.common.primitives.Ints;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;


public class MutualExclusiveProcessor implements AnalysisProcessor {

    private final static CacheKey<Map<MatrixDimensionKey, Map<String, Double>>> CACHEKEY =
            new CacheKey<Map<MatrixDimensionKey, Map<String, Double>>>() {};

    private final MutualExclusiveAnalysis analysis;

    public MutualExclusiveProcessor(MutualExclusiveAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) {

        Date startTime = new Date();

        // Prepare results matrix
        analysis.setResults(new ResourceReference<>("results",
                calculate(
                        monitor,
                        analysis.getData().get(),
                        analysis.getData().get().getLayers().get(analysis.getLayer()),
                        analysis.getTestGroupsModuleMap().get(),
                        analysis.getWeightGroupsModuleMap().get(),
                        analysis.getTestDimension(),
                        analysis.getWeightDimension(),
                        analysis.getIterations(),
                        analysis.getCutoffCmp(),
                        analysis.getCutoff()
                )
        ));

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

    }

    private IMatrix calculate(final IProgressMonitor monitor,
                              final IMatrix data,
                              final IMatrixLayer<Double> dataLayer,
                              final IModuleMap testGroups,
                              final IModuleMap weightGroups,
                              final IMatrixDimension testDimension,
                              final IMatrixDimension weightDimension,
                              final int iterations,
                              final CutoffCmp cutoffCmp, final double cutoff) {

        final IMatrixDimension resultWeightDimension = new HashMatrixDimension(COLUMNS, weightGroups.getModules());
        final IMatrixDimension resultTestDimension = new HashMatrixDimension(ROWS, testGroups.getModules());

        final MutualExclusiveTest test = new MutualExclusiveTest();
        final LayerAdapter<SimpleResult> adapter = new LayerAdapter<>(test.getResultClass());

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                resultTestDimension,
                resultWeightDimension
        );

        CutoffCmpCountAggregator aggregator = new CutoffCmpCountAggregator(cutoffCmp, cutoff);

        String weightGroupInfo;
        int counter = 0;
        for (final String weightGroup : resultWeightDimension) {
            weightGroupInfo = weightGroup + " (" + ++counter + "/" + resultWeightDimension.size() + ")";

            final double[] weights;

            weights = getWeights(monitor, data, dataLayer, testDimension, weightDimension, aggregator, weightGroupInfo);

            if (monitor.isCancelled()) {
                break;
            }


            monitor.begin("Performing test for "  + weightGroupInfo, resultTestDimension.size() * iterations);
            for (String testGroup : resultTestDimension) {
                monitor.info("Group: " + testGroup);

                int coverage = getCoverage(data, dataLayer, testGroups, weightGroups, testDimension, weightDimension, cutoffCmp, cutoff, weightGroup, testGroup);

                int[] draws = getDraws(data, dataLayer, testGroups, testDimension, weightDimension, testGroup, aggregator);

                int signal = 0;
                for (int d : draws) {
                    signal += d;
                }

                if (monitor.isCancelled()) {
                    break;
                }
                //sets monitor.worked
                SimpleResult r = test.processTest(draws, weights, coverage, signal, iterations, monitor);

                if (monitor.isCancelled()) {
                    break;
                }

                adapter.set(resultsMatrix, r, testGroup, weightGroup);
            }
        }

        return resultsMatrix;
    }

    private Map<String, Double> getCachedWeights(IMatrixLayer<Double> dataLayer, IMatrixDimension weightDimension) {
        Map<MatrixDimensionKey, Map<String, Double>> cachedWeights = dataLayer.getCache(CACHEKEY);
        if (cachedWeights == null || cachedWeights.get(weightDimension.getId()) == null) {
            return new HashMap<>();
        }
        return cachedWeights.get(weightDimension.getId());
    }

    private void setCachedWeights(IMatrixLayer<Double> dataLayer, Map<String, Double> cachedWeights, IMatrixDimension weightDimension) {
        Map<MatrixDimensionKey, Map<String, Double>> cache = dataLayer.getCache(CACHEKEY);
        if (cache == null) {
            cache = new HashMap<>();
        }
        cache.put(weightDimension.getId(), cachedWeights);
        dataLayer.setCache(CACHEKEY, cache);
    }

    private double[] getWeights(IProgressMonitor monitor, IMatrix data, final IMatrixLayer<Double> dataLayer, IMatrixDimension testDimension, IMatrixDimension weightDimension, CutoffCmpCountAggregator aggregator, String weightGroupInfo) {

        IMatrixIterable<Double> weightIterator;
        final Map<String, Double> cachedWeights = getCachedWeights(dataLayer, weightDimension);
        int cacheSize = cachedWeights.size();
        final AggregationFunction aggregation = new AggregationFunction(dataLayer, aggregator, testDimension);
        final AbstractMatrixFunction<Double, String> readWeightFunction =
                new AbstractMatrixFunction<Double, String>() {
                    @Override
                    public Double apply(String value, IMatrixPosition position) {
                        Double v;
                        if (cachedWeights.containsKey(value)) {
                            v = cachedWeights.get(value);
                        } else {
                            v = aggregation.apply(value, position);
                            cachedWeights.put(value, v);

                        }
                        return v;
                    }
                };

        weightIterator = data.newPosition().iterate(weightDimension)
            .monitor(monitor, "Calculating weights for " + weightGroupInfo)
            .transform(readWeightFunction)
            .filter(new ValueFilterFunction(dataLayer, CutoffCmp.NE, 0.0, 0.0));

        double[] weights = Doubles.toArray(newArrayList(weightIterator));

        if (cacheSize != cachedWeights.size()) {
            setCachedWeights(dataLayer, cachedWeights, weightDimension);
        }

        return weights;
    }

    /**
     * Calculates the coverage: Which items of the weightDimension have at least
     * one positive event.
     */
    private int getCoverage(IMatrix data, IMatrixLayer<Double> dataLayer, IModuleMap testGroups, IModuleMap weightGroups, IMatrixDimension testDimension, final IMatrixDimension weightDimension, final CutoffCmp cutoffCmp, final double cutoff, String weightGroup, String testGroup) {

        IMatrixIterable<String> it = data.newPosition()
                .iterate(dataLayer,
                        testDimension.subset(testGroups.getMappingItems(testGroup)),
                        weightDimension.subset(weightGroups.getMappingItems(weightGroup)))
                .transform(new AbstractMatrixFunction<String, Double>() {
                    @Override
                    public String apply(Double value, IMatrixPosition position) {
                        return (value != null && cutoffCmp.compare(value, cutoff)) ? position.get(weightDimension) : "";
                    }
                });
        HashSet<String> ids = new HashSet<>();
        for (String id : it) {
            if (id.equals(""))
                continue;

            ids.add(id);
        }
        return ids.size();

    }

    /**
     * Calculate for each item how many positive events are available.
     *
     */
    private int[] getDraws(IMatrix data, IMatrixLayer<Double> dataLayer, IModuleMap testGroups, IMatrixDimension testDimension, IMatrixDimension weightDimension, String testGroup, CutoffCmpCountAggregator aggregator) {

        IMatrixIterable<Integer> it;
            it = data.newPosition()
                    .iterate(testDimension.subset(testGroups.getMappingItems(testGroup)))
                    .transform(new AggregationFunction(dataLayer, aggregator, weightDimension))
                    .transform(new DoubleToIntegerFunction())
                    .filter(new NotNullPredicate<Integer>());
        return Ints.toArray(newArrayList(it));

    }

}
