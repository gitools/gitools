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

import com.google.common.collect.Iterators;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.utils.ComparableComparator;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractIterable<T> implements IMatrixIterable<T> {

    private static int CORES = Runtime.getRuntime().availableProcessors();
    private static ExecutorService EXECUTOR = Executors.newFixedThreadPool(CORES);

    @Override
    public abstract long size();

    @Override
    public abstract IMatrixPosition getPosition();

    @Override
    public IMatrixIterable<T> filter(IMatrixPredicate<T> filter) {
        return new FilterIterable<>(this, filter);
    }

    @Override
    public <F> IMatrixIterable<F> transform(IMatrixFunction<F, T> function) {
        return new TransformIterable<>(this, function);
    }

    @Override
    public IMatrixIterable<T> sort() {
        return sort(ComparableComparator.getInstance());
    }

    @Override
    public IMatrixIterable<T> sort(Comparator<T> comparator) {
        return new SortIterable<>(this, comparator);
    }

    @Override
    public IMatrixIterable<T> monitor(IProgressMonitor monitor, String title) {
        return new MonitorMatrixIterable<>(this, monitor, title);
    }

    @Override
    public IMatrixIterable<T> monitor(IProgressMonitor monitor) {
        return monitor(monitor, null);
    }

    @Override
    public void store(final IMatrix output, final IMatrixPositionMapping mapping, final ILayerAdapter<T> layerAdapter) {

        List<Future<?>> tasks = new ArrayList<>(CORES);
        final Iterator<T> iterator = iterator();
        for (int c=0; c < CORES; c++) {
            tasks.add(EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {

                    IMatrixPosition position = output.newPosition();
                    while (iterator.hasNext()) {
                        T value = iterator.next();
                        mapping.map(getPosition(), position);
                        layerAdapter.set(output, value, position);
                    }
                }
            }));
        }

        IMatrixPosition position = output.newPosition();
        while (iterator.hasNext()) {
            T value = iterator.next();
            mapping.map(getPosition(), position);
            layerAdapter.set(output, value, position);
        }

        // Wait other tasks to finish
        boolean done = false;
        while(!done) {
            done = true;
            for (Future task : tasks) {
                if (task.isDone()) {
                    continue;
                }
                done = false;
            }
        }

    }

    @Override
    public void store(final IMatrix output, final IMatrixPositionMapping mapping, final IMatrixLayer<T> layer) {

        List<Future<?>> tasks = new ArrayList<>(CORES);
        final Iterator<T> iterator = iterator();
        for (int c=0; c < CORES; c++) {
            tasks.add(EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {

                    IMatrixPosition position = output.newPosition();
                    while (iterator.hasNext()) {
                        T value = iterator.next();
                        mapping.map(getPosition(), position);
                        output.set(layer, value, position);
                    }
                }
            }));
        }

        IMatrixPosition position = output.newPosition();
        while (iterator.hasNext()) {
            T value = iterator.next();
            mapping.map(getPosition(), position);
            output.set(layer, value, position);
        }

        // Wait other tasks to finish
        boolean done = false;
        while(!done) {
            done = true;
            for (Future task : tasks) {
                if (task.isDone()) {
                    continue;
                }
                done = false;
            }
        }

    }

    @Override
    public void store(final Collection<T> output) {

        List<Future<?>> tasks = new ArrayList<>(CORES);
        final Iterator<T> iterator = iterator();
        for (int c=0; c < CORES; c++) {
            tasks.add(EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    while (iterator.hasNext()) {
                        output.add(iterator.next());
                    }
                }
            }));
        }

        while (iterator.hasNext()) {
            output.add(iterator.next());
        }

        // Wait other tasks to finish
        boolean done = false;
        while(!done) {
            done = true;
            for (Future task : tasks) {
                if (task.isDone()) {
                    continue;
                }
                done = false;
            }
        }

    }

    @Override
    public void store(IMatrix output, ILayerAdapter<T> layerAdapter) {
        store(output, new NoMapping(), layerAdapter);
    }

    @Override
    public void store(IMatrix output, IMatrixLayer<T> layer) {
        store(output, new NoMapping(), layer);
    }

    @Override
    public int count() {
        return Iterators.size(iterator());
    }
}
