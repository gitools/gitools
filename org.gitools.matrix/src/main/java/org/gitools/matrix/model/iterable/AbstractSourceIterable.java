/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.matrix.model.iterable;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSourceIterable<T> extends AbstractIterable<T> {

    private IMatrixPosition position;
    private IMatrixDimension iterateDimension;

    private List<String> identifiers;

    protected AbstractSourceIterable(IMatrixPosition position, MatrixDimensionKey iterateDimension) {
        this.position = position;
        this.iterateDimension = position.getMatrix().getDimension(iterateDimension);

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

        for (int i = from; i <= to; i++) {
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
