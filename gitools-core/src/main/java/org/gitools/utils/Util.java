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
package org.gitools.utils;

import cern.jet.stat.Probability;

public class Util
{

	/*double zscore = pvalue < 0.5 ? 10.0 : -10.0;
    try {
		if (pvalue > 0.0 && pvalue < 1.0)
			zscore = Probability.normalInverse(1.0 - pvalue);
	}
	catch (IllegalArgumentException e) {}*/

    public static double pvalue2zscore(double pvalue)
    {
        double zscore;
        try
        {
            zscore =
                    pvalue == 0.0 ? 10 :
                            pvalue == 1.0 ? -10 :
                                    Probability.normalInverse(1.0 - pvalue);
        } catch (IllegalArgumentException e)
        {
            zscore = Double.NaN;
        }
        return zscore;
    }

    public static double pvalue2rightzscore(double pvalue)
    {
        double zscore;
        try
        {
            zscore =
                    pvalue == 0.0 ? 10 :
                            pvalue == 1.0 ? 0 :
                                    Probability.normalInverse(1.0 - pvalue);
        } catch (IllegalArgumentException e)
        {
            zscore = pvalue < 0.5 ? 10 : 0;
        }
        return zscore;
    }

    public static String notNullString(String string)
    {
        return string == null ? "" : string;
    }

}
