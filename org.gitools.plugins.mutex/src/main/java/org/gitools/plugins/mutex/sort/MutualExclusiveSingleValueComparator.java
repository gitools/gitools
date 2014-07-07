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
package org.gitools.plugins.mutex.sort;

import org.gitools.api.matrix.*;

import java.util.Comparator;

public class MutualExclusiveSingleValueComparator implements Comparator<String> {

    private IMatrixPosition position;
    private IMatrixLayer<? extends Comparable> layer;
    private IMatrixDimension dimension;
    private SortDirection sortDirection;
    private IMatrixFunction<Comparable, Comparable> transformFunction;

    public MutualExclusiveSingleValueComparator(IMatrixPosition position, IMatrixLayer<? extends Comparable> layer, IMatrixDimension dimension, SortDirection sortDirection,
                                                IMatrixFunction<Comparable, Comparable> transformFunction) {
        this.position = position;
        this.layer = layer;
        this.dimension = dimension;
        this.sortDirection = sortDirection;
        this.transformFunction = transformFunction;
    }

    @Override
    public int compare(String o1, String o2) {


        Comparable v1 = transformFunction.apply(position.getMatrix().get(layer, position.set(dimension, o1)), position);
        Comparable v2 = transformFunction.apply(position.getMatrix().get(layer, position.set(dimension, o2)), position);

        if (v1 == null && v2 == null) {
            return 0;
        }

        if (v1 == null) {
            return 1;
        }

        if (v2 == null) {
            return -1;
        }

        return v1.compareTo(v2) * sortDirection.getFactor();
    }
}
