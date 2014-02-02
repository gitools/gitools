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
import org.apache.commons.collections.comparators.ComparableComparator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.ILayerAdapter;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixFunction;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPositionMapping;
import org.gitools.api.matrix.IMatrixPredicate;

import java.util.Comparator;
import java.util.Set;

public abstract class AbstractIterable<T> implements IMatrixIterable<T> {

    @Override
    public abstract int size();

    @Override
    public abstract IMatrixDimension getIterateDimension();

    @Override
    public abstract IMatrixPosition getPosition();

    @Override
    public IMatrixIterable<T> filter(Set<String> identifiers) {
        return filter(new IdentifiersPredicate<T>(getIterateDimension(), identifiers));
    }

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
        return sort(new ComparableComparator());
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
    public void store(IMatrix output, IMatrixPositionMapping mapping, ILayerAdapter<T> layerAdapter) {

        IMatrixPosition position = output.newPosition();
        for (T value : this) {
            mapping.map(getPosition(), position);
            layerAdapter.set(output, value, position);
        }

    }

    @Override
    public void store(IMatrix output, IMatrixPositionMapping mapping, IMatrixLayer<T> layer) {

        IMatrixPosition position = output.newPosition();
        for (T value : this) {
            mapping.map(getPosition(), position);
            output.set(layer, value, position);
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
