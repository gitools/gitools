package org.gitools.matrix.model.iterable;

import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixPosition;

public class IdentityMatrixFunction<T> extends AbstractMatrixFunction<T, T> {

    @Override
    public T apply(T value, IMatrixPosition position) {
        return value;
    }
}
