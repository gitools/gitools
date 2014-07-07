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
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.MatrixPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractSourceIterable<T> extends AbstractIterable<T> {

    private long size = 1;

    private MatrixPosition mainPosition;

    // Thread safe position
    private ThreadLocal<MatrixPosition> position = new ThreadLocal<MatrixPosition>() {
        @Override
        protected MatrixPosition initialValue() {
            return new MatrixPosition(mainPosition);
        }
    };

    private IMatrixDimension[] iterateDimensions;

    protected AbstractSourceIterable(MatrixPosition position, IMatrixDimension... iterateDimensions) {

        // Iterate all dimensions if any dimension is set
        if (iterateDimensions.length == 0) {

            IMatrix matrix = position.getMatrix();
            MatrixDimensionKey[] keys = matrix.getDimensionKeys();

            iterateDimensions = new IMatrixDimension[keys.length];
            for (int d=0; d < keys.length; d++) {
                iterateDimensions[d] = matrix.getDimension(keys[d]);
            }

        }

        this.mainPosition = position;
        this.iterateDimensions = iterateDimensions;

        for (IMatrixDimension dimension : iterateDimensions) {
            size *= dimension.size();
        }

    }

    public IMatrix getMatrix() {
        return mainPosition.getMatrix();
    }

    @Override
    public IMatrixPosition getPosition() {
        return position.get();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ValueIterator();
    }

    protected abstract T getValue(IMatrixPosition position);

    private class ValueIterator implements Iterator<T> {

        // Current position
        private String[] identifiers;

        // Iteration status
        private int activeDimension;
        private List<Iterator<String>> iterators;
        private boolean hasNext;

        private ValueIterator() {

            int length = iterateDimensions.length;
            this.identifiers = new String[length];
            this.iterators = new ArrayList<>(length);

            try {

                // Iterate to the first position
                for (int d = 0; d < iterateDimensions.length; d++) {

                    Iterator<String> iterator = iterateDimensions[d].iterator();
                    this.iterators.add(iterator);
                    this.identifiers[d] = iterator.next();
                    this.activeDimension = d;
                }

                hasNext = true;

            } catch (NoSuchElementException e) {

                // If there is an empty iterate dimension it's impossible to iterate.
                hasNext = false;

            }

        }

        @Override
        public synchronized boolean hasNext() {
            return this.hasNext;
        }

        @Override
        public synchronized T next() {
            return getValue(nextPosition());
        }

        private synchronized IMatrixPosition nextPosition() {

            // Return current position
            IMatrixPosition position = getPosition();
            for (int d=0; d < iterateDimensions.length; d++) {
                position.set(iterateDimensions[d], identifiers[d]);
            }

            // Iterate to next position
            while(activeDimension >= 0) {
                Iterator<String> activeIterator = iterators.get(activeDimension);
                if (activeIterator.hasNext()) {
                    identifiers[activeDimension] = activeIterator.next();

                    if (activeDimension == iterateDimensions.length - 1) {
                        return position;
                    }

                    activeDimension++;

                } else {
                    iterators.set(activeDimension, iterateDimensions[activeDimension].iterator());
                    activeDimension--;
                }
            }

            hasNext = false;

            return position;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

}
