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
package org.gitools.stats.mtc;

import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;

public class Bonferroni implements MTC
{

    public static String SHORT_NAME = "bonferroni";

    @Override
    public String getName()
    {
        return "Bonferroni";
    }

    @Override
    public String getShortName()
    {
        return SHORT_NAME;
    }

    @Override
    public void correct(DoubleMatrix1D values)
    {
        final int n = values.size();

        values.assign(new DoubleFunction()
        {
            @Override
            public double apply(double v)
            {
                return Math.min(1.0, v * n);
            }
        });
    }

}
