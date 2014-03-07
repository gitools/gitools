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
package org.gitools.utils;

import java.io.Serializable;
import java.util.Comparator;

public class ComparableComparator<T> implements Comparator<T>, Serializable {

    private static final ComparableComparator instance =
            new ComparableComparator();

    public static Comparator getInstance() {
        return instance;
    }

    /**
     * Constructs a ComparableComparator.
     */
    public ComparableComparator() {
    }

    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        else if (o1 == null) {
            return -1;
        }
        else if (o2 == null) {
            return 1;
        }

        if (o1 instanceof Comparable) {
            if (o2 instanceof Comparable) {
                int result1;
                int result2;
                try {
                    result1 = ((Comparable<Object>) o1).compareTo(o2);
                    result2 = ((Comparable<Object>) o2).compareTo(o1);
                }
                catch (ClassCastException e) {
                    return o1.getClass().getName().compareTo(o2.getClass().getName()); // fall back to compare the string
                }

                // enforce comparable contract
                if (result1 == 0 && result2 == 0) {
                    return 0;
                }
                else
                if (result1 < 0 && result2 > 0) { // to make sure the two results are consistent
                    return result1;
                }
                else
                if (result1 > 0 && result2 < 0) { // to make sure the two results are consistent
                    return result1;
                }
                else {
                    // results inconsistent
                    throw new ClassCastException("The two compareTo methods of o1 and o2 returned two inconsistent results. Please make sure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y.");
                }
            }
            else {
                // o2 wasn't comparable
                throw new ClassCastException("The second argument of this method was not a Comparable: " + o2.getClass().getName());
            }
        }
        else if (o2 instanceof Comparable) {
            // o1 wasn't comparable
            throw new ClassCastException("The first argument of this method was not a Comparable: " + o1.getClass().getName());
        }
        else {
            // neither were comparable
            throw new ClassCastException("Both arguments of this method were not Comparables: " + o1.getClass().getName() + " and " + o2.getClass().getName());
        }
    }
}
