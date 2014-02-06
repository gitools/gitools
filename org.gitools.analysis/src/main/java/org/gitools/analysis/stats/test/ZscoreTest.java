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

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.sampling.RandomSampler;
import cern.jet.stat.Probability;
import org.gitools.analysis.stats.calc.Statistic;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.analysis.stats.test.results.ZScoreResult;

import static com.google.common.collect.Iterables.size;

public class ZscoreTest extends AbstractTest {

    protected class PopulationStatistics {
        public int n;
        public double mean;
        public double stdev;
    }

    final Statistic statCalc;
    private Iterable<Double> population;

    private final int numSamples;

    public ZscoreTest(int numSamples, Statistic statCalc) {
        super("zscore-" + statCalc.getName(), ZScoreResult.class);

        this.statCalc = statCalc;
        this.numSamples = numSamples;
    }

    @Override
    public void processPopulation(Iterable<Double> population) {
        this.population = population;
    }


    @Override
    public CommonResult processTest(Iterable<Double> values) {

        double observed;
        double zscore;
        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        // Calculate observed statistic
        observed = statCalc.calc(values);

        PopulationStatistics expected = infereMeanAndStdev(population, values);

        // Calculate zscore and pvalue
        zscore = (observed - expected.mean) / expected.stdev;

        leftPvalue = Probability.normal(zscore);
        rightPvalue = 1.0 - leftPvalue;
        twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
        twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;

        return new ZScoreResult(expected.n, leftPvalue, rightPvalue, twoTailPvalue, observed, expected.mean, expected.stdev, zscore);
    }

    private PopulationStatistics infereMeanAndStdev(Iterable<Double> population, Iterable<Double> values) {

        PopulationStatistics expected = new PopulationStatistics();

        int sampleSize =  size(values);
        int populationSize = size(population);

        long[] lindices = new long[sampleSize];
        int[] indices = new int[sampleSize];

        double sx = 0;
        double sx2 = 0;

        RandomEngine randomEngine = new MersenneTwister();

        for (int i = 0; i < numSamples; i++) {
            RandomSampler.sample(sampleSize, populationSize, sampleSize, 0, lindices, 0, randomEngine);

            copyIndices(lindices, indices);

            //TODO double xi = statCalc.calc(population.viewSelection(indices));
            double xi = statCalc.calc(population);

            sx += xi;
            sx2 += (xi * xi);
        }

        double N = numSamples;

        expected.n = sampleSize;
        expected.mean = sx / N;
        expected.stdev = Math.sqrt((N * sx2) - (sx * sx)) / N;

        return expected;
    }

    private void copyIndices(long[] lindices, int[] indices) {
        for (int j = 0; j < lindices.length; j++)
            indices[j] = (int) lindices[j];
    }

}
