/*
 * #%L
 * gitools-cli
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
package org.gitools.cli.convert;

import cern.jet.stat.Probability;

/**
 * @noinspection ALL
 */
public class TestProbabilitiesTool
{

    public static void main(String[] args)
    {
        System.out.println("k" + "\t" + "n" + "\t" + "p" + "\t" + "lpv" + "\t" + "rpv" + "\t" + "rppv" + "\t" + "tpv" + "\tRl\tRr\tRt");
        int n = 39;
        double p = 0.026426896012509773;
        for (int k = 0; k < n; k++)
        {
            double lpv = Probability.binomial(k, n, p);
            double rpv = k > 0 ? Probability.binomialComplemented(k - 1, n, p) : 1.0;
            double tpv = lpv + rpv > 1.0 ? 1.0 : lpv + rpv;
            double rppv = k > 0 ? Probability.poissonComplemented(k - 1, n * p) : 1.0;
            double rppv2 = 1 - Probability.poisson(k, n * p);
            System.out.println(k + "\t" + n + "\t" + p + "\t" + lpv + "\t" + rpv + "\t" + rppv2 + "\t" + tpv + "\t0\t0\t0");
        }
    }

}
