package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.iterable.AbstractSourceIterable;
import org.gitools.core.matrix.model.matrix.element.LayerAdapter;


public class AdapterSourceIterable<T> extends AbstractSourceIterable<T> {

    private LayerAdapter<T> adapter;

    public AdapterSourceIterable(IMatrixPosition matrixPosition, MatrixDimensionKey dimensionKey, LayerAdapter<T> adapter) {
        super(matrixPosition, dimensionKey);

        this.adapter = adapter;
    }

    @Override
    protected T getValue(IMatrixPosition position) {
        return adapter.get(getMatrix(), position);
    }
}
