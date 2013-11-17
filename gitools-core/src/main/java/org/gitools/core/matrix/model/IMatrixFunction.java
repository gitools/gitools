package org.gitools.core.matrix.model;


public interface IMatrixFunction<F, T> {

    void onBeforeIterate(IMatrixIterable<T> parentIterable);

    void onAfterIterate(IMatrixIterable<T> parentIterable);

    F apply(T value, IMatrixPosition position);

}
