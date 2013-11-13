package org.gitools.core.matrix.model;

public interface IMatrixIterator extends IMatrixPosition {

    IMatrixIterator iterate(IMatrixDimension dimension, Iterable<String> identifiers);

    IMatrixIterator build();

    boolean next(IMatrixDimension dimension);

    void reset(IMatrixDimension dimension);

}
