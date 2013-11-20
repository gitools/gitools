package org.gitools.core.matrix;

import com.google.common.base.Function;

public class NoTransformFunction<T> implements Function<T, T> {

    @Override
    public T apply(T input) {
        return input;
    }
}
