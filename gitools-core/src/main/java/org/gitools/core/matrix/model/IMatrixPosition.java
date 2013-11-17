package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;

public interface IMatrixPosition {

    IMatrix getMatrix();

    String get(IMatrixDimension dimension);

    String get(MatrixDimensionKey dimension);

    IMatrixPosition set(IMatrixDimension dimension, String identifier);

    IMatrixPosition set(MatrixDimensionKey dimension, String identifier);

    IMatrixPosition set(String... identifiers);

    IMatrixIterable<String> iterate(IMatrixDimension dimension);

    IMatrixIterable<String> iterate(MatrixDimensionKey dimensionKey);

    <T> IMatrixIterable<T> iterate(LayerAdapter<T> layerAdapter, IMatrixDimension dimension);

    <T> IMatrixIterable<T> iterate(IMatrixLayer<T> layer, IMatrixDimension dimension);

    String[] toVector();

}
