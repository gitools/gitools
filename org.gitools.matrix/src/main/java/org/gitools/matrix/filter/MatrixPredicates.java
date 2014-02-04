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
package org.gitools.matrix.filter;

import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;


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

    @SafeVarargs
    public static <T> IMatrixPredicate<T> or(IMatrixPredicate<T>... predicates) {
        return new OrPredicate<>(predicates);
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

    public static class OrPredicate<T> implements IMatrixPredicate<T> {

        private IMatrixPredicate<T>[] innerPredicates;

        public OrPredicate(IMatrixPredicate<T>[] innerPredicates) {
            this.innerPredicates = innerPredicates;
        }

        @Override
        public boolean apply(T value, IMatrixPosition position) {

            for (IMatrixPredicate<T> predicate : innerPredicates) {
                if (predicate.apply(value, position)) {
                    return true;
                }
            }

            return false;
        }
    }
}
