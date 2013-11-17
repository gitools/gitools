package org.gitools.core.matrix.model.iterable;

import com.google.common.collect.Iterators;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.gitools.core.analysis.groupcomparison.NoMapping;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.matrix.element.LayerAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;

public abstract class AbstractIterable<T> implements IMatrixIterable<T> {

    @Override
    public abstract int size();

    @Override
    public abstract IMatrixDimension getIterateDimension();

    @Override
    public abstract IMatrixPosition getPosition();

    @Override
    public IMatrixIterable<T> filter(Iterable<String> identifiers) {
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
    public void store(IMatrix output, IMatrixPositionMapping mapping, LayerAdapter<T> layerAdapter) {

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
    public void store(IMatrix output, LayerAdapter<T> layerAdapter) {
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
