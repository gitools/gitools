package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSourceIterable<T> extends AbstractIterable<T> {

    private IMatrixPosition position;
    private IMatrixDimension iterateDimension;

    private List<String> identifiers;

    protected AbstractSourceIterable(IMatrixPosition position, MatrixDimensionKey iterateDimension) {
        this.position = position;
        this.iterateDimension = position.getMatrix().getIdentifiers(iterateDimension);

        this.identifiers = new ArrayList<>(this.iterateDimension.size());
        for (String id : this.iterateDimension) {
            this.identifiers.add(id);
        }
    }

    @Override
    public IMatrixDimension getIterateDimension() {
        return iterateDimension;
    }

    public IMatrix getMatrix() {
        return position.getMatrix();
    }

    @Override
    public IMatrixPosition getPosition() {
        return position;
    }

    @Override
    public int size() {
        return identifiers.size();
    }

    @Override
    public IMatrixIterable<T> from(String fromIdentifier) {
        return between(fromIdentifier, null);
    }

    @Override
    public IMatrixIterable<T> to(String toIdentifier) {
        return between(null, toIdentifier);
    }

    @Override
    public IMatrixIterable<T> between(String fromIdentifier, String toIdentifier) {

        int from = fromIdentifier == null ? 0 : iterateDimension.indexOf(fromIdentifier);
        int to = toIdentifier == null ? iterateDimension.size() - 1 : iterateDimension.indexOf(toIdentifier);

        identifiers = new ArrayList<>(to - from + 1);

        for (int i=from; i <= to; i++) {
            identifiers.add(iterateDimension.getLabel(i));
        }

        return this;
    }

    @Override
    public Iterator<T> iterator() {
         return new ValueIterator(identifiers.iterator());
    }

    protected abstract T getValue(IMatrixPosition position);

    private class ValueIterator implements Iterator<T> {

        private Iterator<String> dimensionIterator;

        private ValueIterator(Iterator<String> dimensionIterator) {
            this.dimensionIterator = dimensionIterator;
        }

        @Override
        public boolean hasNext() {
            return dimensionIterator.hasNext();
        }

        @Override
        public T next() {
            return getValue(nextPosition());
        }

        private IMatrixPosition nextPosition() {
            return getPosition().set(iterateDimension, dimensionIterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }
}
