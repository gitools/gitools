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
package org.gitools.matrix.sort;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.SortDirection;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.Comparator;

public class SingleSortByValueComparator implements Comparator<String> {

    private IMatrixPosition position;
    private IMatrixLayer<? extends Comparable> layer;
    private IMatrixDimension dimension;
    private SortDirection sortDirection;

    public SingleSortByValueComparator(IMatrixPosition position, IMatrixLayer<? extends Comparable> layer, IMatrixDimension dimension, SortDirection sortDirection) {
        this.position = position;
        this.layer = layer;
        this.dimension = dimension;
        this.sortDirection = sortDirection;
    }

    @Override
    public int compare(String o1, String o2) {

        Comparable v1 = position.getMatrix().get(layer, position.set(dimension, o1));
        Comparable v2 = position.getMatrix().get(layer, position.set(dimension, o2));

        if (v1 == null && v2 == null) {
            return 0;
        }

        if (v1 == null) {
            return -1 * sortDirection.getFactor();
        }

        if (v2 == null) {
            return sortDirection.getFactor();
        }

        return v1.compareTo(v2) * sortDirection.getFactor();
    }
}
