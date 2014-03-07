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

import com.google.common.collect.Lists;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;
import org.gitools.analysis.stats.calc.Statistic;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.analysis.stats.test.results.ZScoreResult;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.*;

public class ZscoreTest extends AbstractTest {

    final Statistic statCalc;

    private Random random;
    private List<Double> population;

    private final int numSamples;

    public ZscoreTest(int numSamples, Statistic statCalc) {
        super("zscore-" + statCalc.getName(), ZScoreResult.class);

        this.statCalc = statCalc;
        this.numSamples = numSamples;
    }

    @Override
    public void processPopulation(Iterable<Double> population) {

        Double seed;
        try {
            seed = find(population, notNull());
        } catch (NoSuchElementException e) {
            seed = 1.0;
        }

        this.random = new Random(seed.longValue());
        this.population = Lists.newArrayList(filter(population, notNull()));
    }


    private static NormalDistribution NORMAL = new NormalDistribution();

    @Override
    public CommonResult processTest(Iterable<Double> values) {

        Double observed = statCalc.calc(values);

        if (observed == null) {
            return null;
        }

        int n = size(filter(values, notNull()));
        double sx = 0, sx2 = 0;
        for (int i = 0; i < numSamples; i++) {
            double xi = statCalc.calc(randomSample(population, n));
            sx += xi;
            sx2 += (xi * xi);
        }

        double N = numSamples;
        double mean = sx / N;
        double stdev = FastMath.sqrt((N * sx2) - (sx * sx)) / N;

        double zscore = (observed - mean) / stdev;
        double leftPvalue = NORMAL.cumulativeProbability(zscore);
        double rightPvalue = 1.0 - leftPvalue;
        double twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
        twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;

        return new ZScoreResult(n, leftPvalue, rightPvalue, twoTailPvalue, observed, mean, stdev, zscore);
    }


    public List<Double> randomSample(List<Double> items, int m) {
        for (int i = 0; i < m; i++) {
            int pos = i + random.nextInt(items.size() - i);
            Double tmp = items.get(pos);
            items.set(pos, items.get(i));
            items.set(i, tmp);
        }
        return items.subList(0, m);
    }

}
