package org.gitools.core.matrix.model;

import java.util.Iterator;

public class MatrixIterable<T> implements Iterable<T> {

    private IMatrix matrix;
    private IMatrixLayer<T> layer;
    private IMatrixPosition position;
    private MatrixDimension freeDimension;

    public MatrixIterable(IMatrix matrix, IMatrixLayer<T> layer, MatrixDimension freeDimension) {
        this.matrix = matrix;
        this.layer = layer;
        this.position = new MatrixPosition(matrix.getDimensions());
        this.freeDimension = freeDimension;
    }

    @Override
    public Iterator<T> iterator() {
        return new MatrixIterator(matrix.getIdentifiers(freeDimension));
    }

    public MatrixIterable set(MatrixDimension dimension, String identifier) {
        this.position.set(dimension, identifier);
        return this;
    }

    private class MatrixIterator implements Iterator<T> {

        private Iterator<String> identifiers;

        private MatrixIterator(IMatrixDimension identifiers) {
            this.identifiers = identifiers.iterator();
        }

        @Override
        public boolean hasNext() {
            return identifiers.hasNext();
        }

        @Override
        public T next() {
            return matrix.get(layer, position.set(freeDimension, identifiers.next()));
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }
}
