package org.gitools.core.matrix.filter;

import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPredicate;


public class MatrixPredicates {

    public static <T> IMatrixPredicate<T> notNull() {
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

        @SuppressWarnings("unchecked") // these Object predicates work for any T
        <T> IMatrixPredicate<T> withNarrowedType() {
            return (IMatrixPredicate<T>) this;
        }
    }
}
