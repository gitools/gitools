/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.aggregation;

import org.gitools.utils.cutoffcmp.CutoffCmp;

public class CutoffCmpCountAggregator extends AbstractAggregator {

    private final double cutoff;
    private final CutoffCmp cmp;

    public CutoffCmpCountAggregator(CutoffCmp cmp, double cutoff) {
        super("Count (" + cmp.getAbbreviation() + " " + cutoff + ")");
        this.cmp = cmp;
        this.cutoff = cutoff;
    }

    @Override
    public Double aggregateNoNulls(double[] data) {
        double events = 0.0;
        for (double d : data) {
            if (cmp.compare(d, cutoff)) events++;
        }
        return events;
    }

}
