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
import cern.jet.stat.Probability;
import org.gitools.stats.calc.Statistic;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.jetbrains.annotations.NotNull;

public abstract class ZscoreTest extends AbstractTest
{

    class PopulationStatistics
    {
        public double mean;
        public double stdev;
    }

    final Statistic statCalc;

    private DoubleMatrix1D population;

    ZscoreTest(Statistic statCalc)
    {
        this.statCalc = statCalc;
    }

    @NotNull
    @Override
    public String getName()
    {
        return "zscore-" + statCalc.getName();
    }

    @NotNull
    @Override
    public Class<? extends CommonResult> getResultClass()
    {
        return ZScoreResult.class;
    }

    @Override
    public void processPopulation(String name, DoubleMatrix1D population)
    {
        this.population = population;
    }

    @NotNull
    @Override
    public CommonResult processTest(String condName, @NotNull DoubleMatrix1D condItems, String groupName, int[] groupItemIndices)
    {

        double observed;
        double zscore;
        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        // Create a view with group values (excluding NaN's)

        final DoubleMatrix1D groupItems = condItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);

        // Calculate observed statistic

        observed = statCalc.calc(groupItems);

        // Calculate expected mean and standard deviation from sampling

        int sampleSize = groupItems.size();

        PopulationStatistics expected = new PopulationStatistics();
        infereMeanAndStdev(population, groupItems, expected);

        // Calculate zscore and pvalue
        zscore = (observed - expected.mean) / expected.stdev;

        leftPvalue = Probability.normal(zscore);
        rightPvalue = 1.0 - leftPvalue;
        twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
        twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;

        return new ZScoreResult(sampleSize, leftPvalue, rightPvalue, twoTailPvalue, observed, expected.mean, expected.stdev, zscore);
    }

    protected abstract void infereMeanAndStdev(DoubleMatrix1D population, DoubleMatrix1D groupItems, PopulationStatistics expected);

}
