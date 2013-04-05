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
import jsc.independentsamples.MannWhitneyTest;
import jsc.tests.H1;
import org.gitools.analysis.groupcomparison.GroupComparisonResult;
import org.gitools.stats.test.results.CommonResult;
import org.jetbrains.annotations.NotNull;

public class MannWhitneyWilxoxonTest extends AbstractTest
{

    public MannWhitneyWilxoxonTest()
    {
    }

    @NotNull
    @Override
    public String getName()
    {
        return "mannWhitneyWilcoxon";
    }


    @NotNull
    @Override
    public Class<? extends GroupComparisonResult> getResultClass()
    {
        return GroupComparisonResult.class;
    }

    @Override
    public void processPopulation(String name, DoubleMatrix1D population)
    {
    }


    @NotNull
    public GroupComparisonResult processTest(@NotNull double[] group1, @NotNull double[] group2)
    {

        int g1Nans = 0;
        int g2Nans = 0;

        for (int i = 0; i < group1.length; i++)
        {
            if (Double.isNaN(group1[i]))
            {
                g1Nans++;
            }
        }
        for (int i = 0; i < group2.length; i++)
        {
            if (Double.isNaN(group2[i]))
            {
                g2Nans++;
            }
        }

        double[] group1NoNan = new double[group1.length - g1Nans];
        double[] group2NoNan = new double[group2.length - g2Nans];

        int offset = 0;
        for (int i = 0; i < group1.length; i++)
        {
            if (!Double.isNaN(group1[i]))
            {
                group1NoNan[i - offset] = group1[i];
            }
            else
            {
                offset++;
            }
        }
        offset = 0;
        for (int i = 0; i < group2.length; i++)
        {
            if (!Double.isNaN(group2[i]))
            {
                group2NoNan[i - offset] = group2[i];
            }
            else
            {
                offset++;
            }
        }

        if (group1NoNan.length > 1 && group2NoNan.length > 1)
        {
            MannWhitneyTest mwwLeft = new MannWhitneyTest(group1NoNan, group2NoNan, H1.LESS_THAN);
            MannWhitneyTest mwwRight = new MannWhitneyTest(group1NoNan, group2NoNan, H1.GREATER_THAN);
            MannWhitneyTest mwwTwoTail = new MannWhitneyTest(group1NoNan, group2NoNan, H1.NOT_EQUAL);


            return new GroupComparisonResult(mwwLeft.getN(), group1NoNan.length, group2NoNan.length, mwwLeft.getSP(), mwwRight.getSP(), mwwTwoTail.getSP());
        }
        else
        {
            return new GroupComparisonResult(group1NoNan.length + group2NoNan.length, group1NoNan.length, group2NoNan.length, Double.NaN, Double.NaN, Double.NaN);
        }

    }

    @NotNull
    @Override
    public CommonResult processTest(String condName, DoubleMatrix1D condItems, String groupName, int[] groupItemIndices)
    {
        throw new UnsupportedOperationException("Not supported at all.");
    }

}
