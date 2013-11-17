package org.gitools.core.matrix.model;

public interface IMatrixPredicate<T> {

    boolean apply(T value, IMatrixPosition position);
}
