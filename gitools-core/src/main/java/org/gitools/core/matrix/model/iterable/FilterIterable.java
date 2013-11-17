package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrixPredicate;

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
