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
package org.gitools.analysis._DEPRECATED.matrix.filter;

import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.position.IMatrixPredicate;


public class MatrixPredicates {

    public static <T> IMatrixPredicate<T> notNull(Class<T> type) {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }

    public static <T> IMatrixPredicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }

    public static <T> IMatrixPredicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }

    public static <T> IMatrixPredicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    enum ObjectPredicate implements IMatrixPredicate<Object> {
        ALWAYS_TRUE {
            @Override
            public boolean apply(Object o, IMatrixPosition position) {
                return false;
            }
        },
        ALWAYS_FALSE {
            @Override
            public boolean apply(Object o, IMatrixPosition position) {
                return false;
            }
        },
        IS_NULL {
            @Override
            public boolean apply(Object o, IMatrixPosition position) {
                return o == null;
            }
        },
        NOT_NULL {
            @Override
            public boolean apply(Object o, IMatrixPosition position) {
                return o != null;
            }
        };

        @SuppressWarnings("unchecked")
            // these Object predicates work for any T
        <T> IMatrixPredicate<T> withNarrowedType() {
            return (IMatrixPredicate<T>) this;
        }
    }
}
