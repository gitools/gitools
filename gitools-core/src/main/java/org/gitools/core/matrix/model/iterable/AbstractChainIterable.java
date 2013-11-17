package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixIterable;
import org.gitools.core.matrix.model.IMatrixPosition;

import java.util.Iterator;

public abstract class AbstractChainIterable<F, T> extends AbstractIterable<F> {

    private static String ERROR_MESSAGE = "You must limit the iterable before operate on it.";

    private AbstractIterable<T> parentIterable;

    protected AbstractChainIterable(AbstractIterable<T> parentIterable) {
        this.parentIterable = parentIterable;
    }

    protected Iterator<T> newParentIterator() {
        return parentIterable.iterator();
    }

    @Override
    public int size() {
        return parentIterable.size();
    }

    @Override
    public IMatrixDimension getIterateDimension() {
        return parentIterable.getIterateDimension();
    }

    @Override
    public IMatrixPosition getPosition() {
        return parentIterable.getPosition();
    }

    public IMatrix getMatrix() {
        return getPosition().getMatrix();
    }

    @Override
    public IMatrixIterable<F> from(String fromIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public IMatrixIterable<F> to(String toIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public IMatrixIterable<F> between(String fromIdentifier, String toIdentifier) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }
}
