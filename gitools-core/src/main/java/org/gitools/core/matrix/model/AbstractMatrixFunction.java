package org.gitools.core.matrix.model;

public abstract class AbstractMatrixFunction<F, T> implements IMatrixFunction<F, T> {

    @Override
    public void onAfterIterate(IMatrixIterable<T> parentIterable) {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<T> parentIterable) {
    }

    @Override
    public abstract F apply(T value, IMatrixPosition position);
}
