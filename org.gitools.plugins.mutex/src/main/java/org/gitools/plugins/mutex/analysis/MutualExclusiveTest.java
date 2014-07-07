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

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.gitools.analysis.stats.test.WeightedRandPerm;
import org.gitools.api.analysis.IProgressMonitor;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class MutualExclusiveTest {

    private static final NormalDistribution NORMAL = new NormalDistribution();
    ;
    Random random;
    private String name;


    public MutualExclusiveTest() {
        name = "mutualExclusive";
        this.random = new Random(849);
    }

    public Class getResultClass() {
        return MutualExclusiveResult.class;
    }

    /**
     * @param draws      Array with count of events per item
     * @param weights
     * @param coverage
     * @param signal
     * @param iterations
     * @param monitor
     * @return
     */
    public MutualExclusiveResult processTest(int[] draws, double[] weights, int coverage, int signal, int iterations, IProgressMonitor monitor) {

        int samplesCount = weights.length;
        WeightedRandPerm permutator = new WeightedRandPerm(random, weights);

        double[] measurements = new double[iterations];
        int greaterEvents = 0;
        int smallerEvents = 0;
        int equalEevents = 0;
        for (int i = 0; i < iterations; i++) {
            measurements[i] = simulation(samplesCount, draws, permutator);
            if (measurements[i] > coverage) {
                greaterEvents++;
            } else if (measurements[i] < coverage) {
                smallerEvents++;
            } else {
                equalEevents++;
            }
            if ((i + 1) % 100 == 0) {
                monitor.worked(100);
                if (monitor.isCancelled()) {
                    break;
                }
            }
        }

        int mutexEvents = greaterEvents + equalEevents;
        int coocEvents = smallerEvents + equalEevents;

        double var = StatUtils.variance(measurements);
        double mean = StatUtils.mean(measurements);

        double zscore = (coverage - mean) / Math.sqrt(var);
        double coocP = NORMAL.cumulativeProbability(zscore);   // leftPValue
        double mutexP = 1.0 - coocP;                           // rightPValue

        return new MutualExclusiveResult(samplesCount, zscore, mutexP, coocP, coverage, signal, mean, var, mutexEvents, coocEvents);

    }


    private double simulation(int samplesCount, int[] drawsList, WeightedRandPerm random) {
        Set<Integer> set = new HashSet<>();

        for (int draws : drawsList) {
            random.reset(samplesCount);
            for (int i = 0; i < draws; i++) {
                set.add(random.next());
            }
        }

        return set.size();

    }

}
