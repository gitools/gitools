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
package org.gitools.analysis._DEPRECATED.matrix.sort;


import com.google.common.base.Function;
import org.gitools.api.matrix.SortDirection;

import java.util.Comparator;

public class SortByLabelComparator implements Comparator<String> {

    private SortDirection direction;
    private Function<String, String> transformFunction;
    private boolean asNumeric;

    public SortByLabelComparator(SortDirection direction, Function<String, String> transformFunction, boolean asNumeric) {
        this.direction = direction;
        this.transformFunction = transformFunction;
        this.asNumeric = asNumeric;
    }

    @Override
    public int compare(String o1, String o2) {

        String v1 = transformFunction.apply(o1);
        String v2 = transformFunction.apply(o2);

        if (asNumeric) {
            return direction.compare(Double.valueOf(v1), Double.valueOf(v2));
        }

        return direction.compare(v1, v2);
    }
}
