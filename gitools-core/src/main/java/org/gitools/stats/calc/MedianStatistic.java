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
package org.gitools.stats.calc;

import cern.colt.matrix.DoubleMatrix1D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MedianStatistic implements Statistic {

    @NotNull
    @Override
    public String getName() {
        return "median";
    }

    @Override
    public double calc(@NotNull DoubleMatrix1D values) {

        final int size = values.size();

        if (size == 0) {
            return Double.NaN;
        } else if (size == 1) {
            return values.getQuick(0);
        }

        double[] tmp = new double[size];

        values.toArray(tmp);

        Arrays.sort(tmp);

        final int middle = size / 2;
        double median = tmp[middle];

        if (size % 2 == 0) {
            median = (tmp[middle - 1] + median) / 2.0;
        }

        return median;
    }

}
