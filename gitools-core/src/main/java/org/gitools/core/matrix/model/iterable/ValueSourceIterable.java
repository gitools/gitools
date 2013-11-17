package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.MatrixDimensionKey;

public class ValueSourceIterable<T> extends AbstractSourceIterable<T> {

    private IMatrixLayer<T> layer;

    public ValueSourceIterable(IMatrixPosition position, MatrixDimensionKey iterateDimension, IMatrixLayer<T> layer) {
        super(position, iterateDimension);

        this.layer = layer;
    }

    @Override
    protected T getValue(IMatrixPosition position) {
        return getMatrix().get(layer, position);
    }
}
