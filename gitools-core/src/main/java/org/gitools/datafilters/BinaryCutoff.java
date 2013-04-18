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
package org.gitools.datafilters;

import cern.colt.function.DoubleFunction;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import java.io.Serializable;

/**
 * @noinspection ALL
 */
public class BinaryCutoff implements DoubleFunction, Serializable {

    private static final long serialVersionUID = 5091376519840044515L;

    private final CutoffCmp cmp;
    private final double cutoff;

    public BinaryCutoff(CutoffCmp cmp, double cutoff) {
        this.cmp = cmp;
        this.cutoff = cutoff;
    }

    @Override
    public double apply(double value) {
        return Double.isNaN(value) ? Double.NaN : cmp.compare(value, cutoff) ? 1 : 0;
    }
}
