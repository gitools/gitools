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
package org.gitools.core.matrix.model.iterable;

import org.gitools.api.matrix.position.IMatrixPredicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilterIterable<T> extends AbstractChainIterable<T, T> {

    private IMatrixPredicate<T> filter;

    public FilterIterable(AbstractIterable<T> parentIterable, IMatrixPredicate<T> filter) {
        super(parentIterable);

        this.filter = filter;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilterIterator(newParentIterator());
    }

    private class FilterIterator extends AbstractChainIterator<T, T> {

        private boolean hasNext = false;
        private T nextValue;

        public FilterIterator(Iterator<T> parentIterator) {
            super(parentIterator);
        }

        @Override
        public boolean hasNext() {

            if (super.hasNext()) {

                nextValue = parentNext();

                while (!filter.apply(nextValue, getPosition())) {
                    if (!super.hasNext()) {
                        hasNext = false;
                        return hasNext;
                    }

                    nextValue = parentNext();
                }

                hasNext = true;
            } else {
                hasNext = false;
            }

            return hasNext;
        }

        @Override
        public T next() {

            if (!hasNext && !hasNext()) {
                throw new NoSuchElementException();
            }

            return nextValue;
        }
    }
}
