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
package org.gitools.core.matrix.model;

import org.apache.commons.collections.comparators.ComparableComparator;

/**
* Created with IntelliJ IDEA.
* User: jordi
* Date: 18/11/13
* Time: 12:48
* To change this template use File | Settings | File Templates.
*/
public enum SortDirection {
    ASCENDING("Ascending", 1),
    DESCENDING("Descending", -1);

    private final String title;
    private final int factor;

    SortDirection(String title, int factor) {
        this.title = title;
        this.factor = factor;
    }

    public int getFactor() {
        return factor;
    }

    public int compare(Object o1, Object o2) {
        if(o1 == o2) { return 0; }
        if(o1 == null) { return ( factor ); }
        if(o2 == null) { return ( -1 * factor ); }
        return ComparableComparator.getInstance().compare(o1, o2) * factor;
    }

    @Override
    public String toString() {
        return title;
    }
}
