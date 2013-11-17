package org.gitools.core.matrix.model;


public interface IMatrixFunction<F, T> {

    F apply(T value, IMatrixPosition position);

}
