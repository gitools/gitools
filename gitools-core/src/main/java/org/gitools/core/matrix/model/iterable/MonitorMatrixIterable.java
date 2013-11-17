package org.gitools.core.matrix.model.iterable;

import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Iterator;

public class MonitorMatrixIterable<T> extends AbstractChainIterable<T, T> {

    private IProgressMonitor monitor;
    private String title;

    public MonitorMatrixIterable(AbstractIterable<T> parentIterable, IProgressMonitor monitor, String title) {
        super(parentIterable);

        this.monitor = monitor;
        this.title = title;
    }

    @Override
    public Iterator<T> iterator() {
        monitor.begin(title, size());
        return new MonitorIterator<T>(newParentIterator());
    }

    private class MonitorIterator<T> extends AbstractChainIterator<T, T> {

        public MonitorIterator(Iterator<T> parentIterator) {
            super(parentIterator);
        }

        @Override
        public T next() {
            monitor.worked(1);
            return parentNext();
        }
    }
}
