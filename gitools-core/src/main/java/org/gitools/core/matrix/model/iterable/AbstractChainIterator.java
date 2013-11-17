package org.gitools.core.matrix.model.iterable;

import java.util.Iterator;

public abstract class AbstractChainIterator<F, T> implements Iterator<F> {

    private Iterator<T> parentIterator;

    public AbstractChainIterator(Iterator<T> parentIterator) {
        this.parentIterator = parentIterator;
    }

    @Override
    public boolean hasNext() {
        return parentIterator.hasNext();
    }

    protected T parentNext() {
        return parentIterator.next();
    }

    @Override
    public void remove() {
        parentIterator.remove();
    }
}
