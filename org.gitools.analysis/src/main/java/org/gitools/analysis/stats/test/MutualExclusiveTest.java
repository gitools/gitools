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
package org.gitools.analysis.stats.test;

import org.apache.commons.math3.stat.StatUtils;
import org.gitools.analysis.stats.test.results.MutualExclusiveResult;
import org.gitools.analysis.stats.test.results.SimpleResult;
import org.gitools.api.analysis.IProgressMonitor;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class MutualExclusiveTest extends AbstractTest {

    Random random;


    public MutualExclusiveTest() {
        super("mutualExclusive", MutualExclusiveResult.class);
        this.random = new Random(849);
    }

    @Override
    public Class<? extends SimpleResult> getResultClass() {
        return MutualExclusiveResult.class;
    }

    /**
     *
     *
     * @param draws Array with count of events per item
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
        int events = 0;
        for (int i = 0; i < iterations; i++) {
            measurements[i] = simulation(samplesCount, draws, permutator);
            if (measurements[i] >= coverage) {
                events++;
            }
            if ((i+1) % 100 == 0) {
                monitor.worked(100);
                if (monitor.isCancelled()) {
                    break;
                }
            }
        }

        double p = (double) events / (double) iterations;

        double var = StatUtils.variance(measurements);
        double mean = StatUtils.mean(measurements);

        return new MutualExclusiveResult(samplesCount, p, coverage, signal, mean, var);

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
