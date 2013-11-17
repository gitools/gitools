package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.MatrixDimensionKey;

public class IdentifierSourceIterable extends AbstractSourceIterable<String> {

    public IdentifierSourceIterable(IMatrixPosition position, MatrixDimensionKey iterateDimension) {
        super(position, iterateDimension);
    }

    @Override
    protected String getValue(IMatrixPosition position) {
        return position.get(getIterateDimension());
    }
}
