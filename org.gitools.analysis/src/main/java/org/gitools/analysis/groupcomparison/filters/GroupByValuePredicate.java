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
package org.gitools.analysis.groupcomparison.filters;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.position.IMatrixPredicate;
import org.gitools.utils.datafilters.BinaryCutoff;

public class GroupByValuePredicate implements IMatrixPredicate<Double> {

    private IMatrixLayer<Double> cutoffLayer;
    private BinaryCutoff binaryCutoff;
    private Double nullValue;

    public GroupByValuePredicate(IMatrixLayer<Double> cutoffLayer, BinaryCutoff binaryCutoff, Double nullValue) {
        this.cutoffLayer = cutoffLayer;
        this.binaryCutoff = binaryCutoff;
        this.nullValue = nullValue;
    }

    public boolean apply(Double value, IMatrixPosition position) {

        Double cutoffValue = position.getMatrix().get(cutoffLayer, position);

        if (cutoffValue == null) {
            cutoffValue = nullValue;
        }

        return (cutoffValue != null) && (binaryCutoff.apply(cutoffValue) == 1.0);
    }

}
