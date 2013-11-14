package org.gitools.core.matrix.model;

public interface IMatrixPosition {

    String get(MatrixDimension dimension);

    IMatrixPosition set(MatrixDimension dimension, String identifier);

    String[] toVector();

}
