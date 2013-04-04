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
package org.gitools.stats.test;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.sampling.RandomSampler;
import org.gitools.stats.calc.Statistic;
import org.jetbrains.annotations.NotNull;

public class ZscoreWithSamplingTest extends ZscoreTest
{

    protected int numSamples;

    public ZscoreWithSamplingTest(int numSamples, Statistic statCalc)
    {
        super(statCalc);
        this.numSamples = numSamples;
    }

    @Override
    protected void infereMeanAndStdev(
            @NotNull DoubleMatrix1D population, @NotNull DoubleMatrix1D groupItems, @NotNull PopulationStatistics expected)
    {

        final int sampleSize = groupItems.size();

        long[] lindices = new long[sampleSize];
        int[] indices = new int[sampleSize];

        double sx = 0;
        double sx2 = 0;

        RandomEngine randomEngine = new MersenneTwister();

        for (int i = 0; i < numSamples; i++)
        {
            RandomSampler.sample(
                    sampleSize, population.size(),
                    sampleSize, 0, lindices, 0,
                    randomEngine);

            copyIndices(lindices, indices);

            double xi = statCalc.calc(
                    population.viewSelection(indices));

            sx += xi;
            sx2 += (xi * xi);
        }

        double N = numSamples;

        expected.mean = sx / N;
        expected.stdev = Math.sqrt((N * sx2) - (sx * sx)) / N;
        //Math.sqrt((sx2 - N * (expectedMean * expectedMean)) / (N - 1));
    }

    private final void copyIndices(@NotNull long[] lindices, int[] indices)
    {
        for (int j = 0; j < lindices.length; j++)
            indices[j] = (int) lindices[j];
    }
}
