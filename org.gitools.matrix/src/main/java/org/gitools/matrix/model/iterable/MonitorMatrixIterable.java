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

import org.gitools.api.analysis.IProgressMonitor;

import java.util.Iterator;
import java.util.concurrent.CancellationException;

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

        if (title != null) {
            monitor.begin(title, size());
        }

        return new MonitorIterator<>(newParentIterator());
    }

    private class MonitorIterator<T> extends AbstractChainIterator<T, T> {

        public MonitorIterator(Iterator<T> parentIterator) {
            super(parentIterator);
        }

        @Override
        public T next() {

            if (monitor.isCancelled()) {
                throw new CancellationException();
            }

            monitor.worked(1);
            return parentNext();
        }
    }
}
