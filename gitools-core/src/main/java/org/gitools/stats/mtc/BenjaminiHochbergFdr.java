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

import cern.colt.matrix.DoubleMatrix1D;
import org.jetbrains.annotations.NotNull;

public class BenjaminiHochbergFdr implements MTC
{

    @NotNull
    public static final String SHORT_NAME = "bh";

    @NotNull
    @Override
    public String getName()
    {
        return "Benjamini Hochberg FDR";
    }

    @NotNull
    @Override
    public String getShortName()
    {
        return SHORT_NAME;
    }

    @Override
    public void correct(@NotNull final DoubleMatrix1D values)
    {

        DoubleMatrix1D sortedValues = values.viewSorted();

        int m = sortedValues.size();
        double lastP = -1;
        int rank = 0;
        for (int idx = 0; idx < m; idx++)
        {
            double p = sortedValues.get(idx);
            if (p != lastP)
            {
                rank = rank + 1;
            }
            sortedValues.set(idx, Math.min(1.0, p * m / rank));
            lastP = p;
        }
    }
}
