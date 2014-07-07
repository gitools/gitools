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


import com.google.common.base.Function;
import org.gitools.api.matrix.SortDirection;
import org.gitools.api.matrix.view.IMatrixViewDimension;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SortByLabelComparator implements Comparator<String> {

    private IMatrixViewDimension sortDimension;
    private SortDirection direction;
    protected Function<String, String> transformFunction;
    private int firstPosition;
    private boolean asNumeric;
    private Set<String> sortIdentifiers;

    /**
     * @param sortDimension
     * @param direction         Sort direction
     * @param transformFunction Patterns transform function
     * @param firstPosition     Indicate if only sorting selected items, otherwise -1
     * @param asNumeric         If the annotation values are numeric
     */
    public SortByLabelComparator(IMatrixViewDimension sortDimension, SortDirection direction, Function<String, String> transformFunction, int firstPosition, boolean asNumeric) {
        this.sortDimension = sortDimension;
        this.direction = direction;
        this.transformFunction = transformFunction;
        this.firstPosition = firstPosition;
        this.asNumeric = asNumeric;

        sortIdentifiers = new HashSet<>();
        if (firstPosition > -1) {
            for (String id : sortDimension.getSelected()) {
                sortIdentifiers.add(id);
            }
        }
    }

    @Override
    public int compare(String idx1, String idx2) {


        // 1. If only sorting selected and idx1 & idx2 not amognst them
        if (firstPosition > -1) {
            boolean before1 = sortDimension.indexOf(idx1) < firstPosition;
            boolean before2 = sortDimension.indexOf(idx2) < firstPosition;


            if (!sortIdentifiers.contains(idx1) && !sortIdentifiers.contains(idx2)) {
                if (before1 != before2) {
                    return before1 ? -1 : 1;
                } else {
                    return 0;
                }
            } else if (!sortIdentifiers.contains(idx1)) {
                return before1 ? -1 : 1;
            } else if (!sortIdentifiers.contains(idx2)) {
                return before2 ? 1 : -1;
            }
        }


        // 2 Compare selected or ALL
        String v1 = transformFunction.apply(idx1);
        String v2 = transformFunction.apply(idx2);

        if (v1 == null && v2 == null) {
            return 0;
        }

        if (v1 == null) {
            return 1;
        }

        if (v2 == null) {
            return -1;
        }

        if (asNumeric) {
            return direction.compare(Double.valueOf(v1), Double.valueOf(v2));
        }

        return direction.compare(v1, v2);
    }
}
