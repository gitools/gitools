package org.gitools.core.matrix.filter;

import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPredicate;

public class NullPredicate<T> implements IMatrixPredicate<T> {

    @Override
    public boolean apply(T value, IMatrixPosition position) {
        return value == null;
    }
}
