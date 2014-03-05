/*
 * #%L
 * org.gitools.utils
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.utils.cutoffcmp;

import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixPosition;

public class CutoffMatrixFunction extends AbstractMatrixFunction<Double, Double> {

    private CutoffCmp comparator;
    private double cutoff;

    private static Double ZERO = 0.0;
    private static Double ONE = 1.0;

    public CutoffMatrixFunction(CutoffCmp comparator, double cutoff) {
        assert comparator != null : "The cutoff comparator cannot be NULL";

        this.comparator = comparator;
        this.cutoff = cutoff;
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {

        if (value == null) {
            return value;
        }

        return comparator.compare(value, cutoff) ? ONE : ZERO;
    }
}
