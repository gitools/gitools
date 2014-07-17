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
package org.gitools.plugins.mutex.analysis;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.matrix.filter.NotNullPredicate;
import org.gitools.matrix.filter.ValueFilterFunction;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.sort.AggregationFunction;
import org.gitools.utils.aggregation.NonNullCountAggregator;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;


public class MutualExclusiveProcessor implements AnalysisProcessor {

    private final static Key<MutexWeightCache> CACHEKEY = new Key<MutexWeightCache>() {
    };

    private final MutualExclusiveAnalysis analysis;

    public MutualExclusiveProcessor(MutualExclusiveAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) {

        Date startTime = new Date();

        // Prepare results matrix

        IMatrix results = calculate(
                monitor,
                analysis.getData().get(),
                analysis.getData().get().getLayers().get(analysis.getLayer()),
                analysis.getTestGroupsModuleMap().get(),
                analysis.getWeightGroupsModuleMap().get(),
                analysis.getTestDimension(),
                analysis.getWeightDimension(),
                analysis.getIterations(),
                analysis.getEventFunction(),
                analysis.isDiscardEmpty()
        );

        analysis.setResults(new ResourceReference<>("results", new Heatmap(results)));
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
                              NonEventToNullFunction eventFunction, boolean discardEmpty) {

        final IMatrixDimension resultWeightDimension = new HashMatrixDimension(COLUMNS, weightGroups.getModules());
        final IMatrixDimension resultTestDimension = new HashMatrixDimension(ROWS, testGroups.getModules());

        final MutualExclusiveTest test = new MutualExclusiveTest();
        final LayerAdapter<MutualExclusiveResult> adapter = new LayerAdapter<>(MutualExclusiveResult.class);

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                resultTestDimension,
                resultWeightDimension
        );

        String weightGroupInfo;
        int counter = 0;
        for (final String weightGroup : resultWeightDimension) {

            ++counter;

            double[] weights = new double[0];
            weightGroupInfo = weightGroup + " (" + counter + "/" + resultWeightDimension.size() + ")";
            boolean weightGroupInfoSet = false;
            IMatrixDimension weightDimensionSubset = null;

            for (String testGroup : resultTestDimension) {

                IMatrixDimension testDimensionSubset = testDimension.subset(testGroups.getMappingItems(testGroup));

                Set<String> samples = weightGroups.getMappingItems(weightGroup);
                if (discardEmpty) {
                    samples = getNonEmptySamples(data, dataLayer, testDimensionSubset, weightDimension.subset(samples), eventFunction);
                    weightDimensionSubset = weightDimension.subset(samples);
                    weights = getWeights(monitor, data, dataLayer, testDimension, weightDimensionSubset, weightGroupInfo + ": " + testGroup, eventFunction);
                } else if (weights.length == 0) {
                    weightDimensionSubset = weightDimension.subset(samples);
                    weights = getWeights(monitor, data, dataLayer, testDimension, weightDimensionSubset, weightGroupInfo, eventFunction);
                }

                if (!weightGroupInfoSet) {
                    monitor.begin("Performing test for " + weightGroupInfo, resultTestDimension.size() * iterations);
                    weightGroupInfoSet = true;
                }


                if (monitor.isCancelled()) {
                    break;
                }

                monitor.info("Group: " + testGroup);

                int coverage = getCoverage(data, dataLayer, testDimensionSubset, weightDimensionSubset, eventFunction);

                int[] draws = getDraws(data, dataLayer, testDimensionSubset, weightDimensionSubset, eventFunction);

                int signal = 0;
                for (int d : draws) {
                    signal += d;
                }

                if (monitor.isCancelled()) {
                    break;
                }
                //sets monitor.worked
                MutualExclusiveResult r = new MutualExclusiveTest().processTest(draws, weights, coverage, signal, iterations, monitor);

                if (monitor.isCancelled()) {
                    break;
                }

                adapter.set(resultsMatrix, r, testGroup, weightGroup);
            }
        }

        return resultsMatrix;
    }

    private Set<String> getNonEmptySamples(IMatrix data, IMatrixLayer<Double> dataLayer, IMatrixDimension testDimensionSubset, IMatrixDimension weightDimensionSubset, NonEventToNullFunction eventFunction) {

        //Set<String> samples = new HashSet<>();

        IMatrixIterable<String> iterable = data.newPosition()
                .iterate(weightDimensionSubset)
                .transform(new AggregationFunction(dataLayer, NonNullCountAggregator.INSTANCE, testDimensionSubset, eventFunction))
                .transform(new DoubleToIntegerFunction())
                .filter(new NotNullPredicate<Integer>())
                .transform(new DimensionIdFunction(weightDimensionSubset));

        ArrayList<String> samples = newArrayList(iterable);

        return new HashSet<>(samples);
    }

    private Map<String, Double> getCachedWeights(IMatrixLayer<Double> dataLayer,
                                                 IMatrixDimension weightDimension,
                                                 NonEventToNullFunction<?> eventFunction) {
        MutexWeightCache mutexCache = dataLayer.getCache(CACHEKEY);
        if (mutexCache == null) {
            mutexCache = new MutexWeightCache();
        }


        return mutexCache.getCacheWeights(
                createFingerprint(eventFunction, weightDimension));

    }

    private String createFingerprint(NonEventToNullFunction<?> eventFunction, IMatrixDimension weightDimension) {
        return weightDimension.getId().getLabel() + "-" + eventFunction.getDescription();
    }

    private void setCachedWeights(IMatrixLayer<Double> dataLayer,
                                  Map<String, Double> cachedWeights,
                                  IMatrixDimension weightDimension,
                                  NonEventToNullFunction function) {
        MutexWeightCache cache = dataLayer.getCache(CACHEKEY);
        if (cache == null) {
            cache = new MutexWeightCache();
        }
        cache.setCacheWeights(createFingerprint(function, weightDimension), cachedWeights);
        dataLayer.setCache(CACHEKEY, cache);
    }

    private double[] getWeights(IProgressMonitor monitor, IMatrix data, final IMatrixLayer<Double> dataLayer, IMatrixDimension testDimension, IMatrixDimension weightDimension, String weightGroupInfo, final NonEventToNullFunction<?> eventFunction) {

        IMatrixIterable<Double> weightIterator;
        final Map<String, Double> cachedWeights = getCachedWeights(dataLayer, weightDimension, eventFunction);
        int cacheSize = cachedWeights.size();
        final AggregationFunction aggregation = new AggregationFunction(dataLayer, NonNullCountAggregator.INSTANCE, testDimension, eventFunction);

        final AbstractMatrixFunction<Double, String> readWeightFunction =
                new AbstractMatrixFunction<Double, String>() {
                    @Override
                    public Double apply(String identifier, IMatrixPosition position) {
                        Double v;
                        if (cachedWeights.containsKey(identifier)) {
                            v = cachedWeights.get(identifier);
                        } else {
                            v = aggregation.apply(identifier, position);
                            cachedWeights.put(identifier, v);
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
            setCachedWeights(dataLayer, cachedWeights, weightDimension, eventFunction);
        }

        return weights;
    }

    /**
     * Calculates the coverage: Which items of the weightDimension have at least
     * one positive event.
     */
    private int getCoverage(IMatrix data, IMatrixLayer<Double> dataLayer, IMatrixDimension testDimension, final IMatrixDimension weightDimension, NonEventToNullFunction<?> eventFunction) {

        IMatrixIterable<String> it = data.newPosition()
                .iterate(dataLayer,
                        testDimension,
                        weightDimension)
                .transform(eventFunction)
                .transform(new AbstractMatrixFunction<String, Double>() {
                    @Override
                    public String apply(Double value, IMatrixPosition position) {
                        return (value != null) ? position.get(weightDimension) : "";
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
     */
    private int[] getDraws(IMatrix data, IMatrixLayer<Double> dataLayer, IMatrixDimension testDimension, IMatrixDimension weightDimension, NonEventToNullFunction<?> eventFunction) {

        IMatrixIterable<Integer> it;
        it = data.newPosition()
                .iterate(testDimension)
                .transform(new AggregationFunction(dataLayer, NonNullCountAggregator.INSTANCE, weightDimension, eventFunction))
                .transform(new DoubleToIntegerFunction())
                .filter(new NotNullPredicate<Integer>());
        return Ints.toArray(newArrayList(it));

    }


    private class MutexWeightCache {

        //1st String: DimensionID + event description
        //2nd String id of row or col
        //Double weight
        Map<String, Map<String, Double>> cacheWeightsCatalog;

        public MutexWeightCache() {
            cacheWeightsCatalog = new HashMap<>();
        }

        public Map<String, Double> getCacheWeights(String fingerprint) {
            if (cacheWeightsCatalog == null || !cacheWeightsCatalog.containsKey(fingerprint)) {
                return new HashMap<>();
            }

            return cacheWeightsCatalog.get(fingerprint);
        }

        public void setCacheWeights(String fingerprint, Map<String, Double> cacheWeights) {
            this.cacheWeightsCatalog.put(fingerprint, cacheWeights);
        }
    }

}
