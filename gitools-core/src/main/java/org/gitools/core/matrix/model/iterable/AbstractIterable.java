package org.gitools.core.matrix.model.iterable;

import org.gitools.core.analysis.groupcomparison.NoMapping;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.matrix.element.LayerAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

public abstract class AbstractIterable<T> implements IMatrixIterable<T> {

    public abstract int size();

    public abstract IMatrixDimension getIterateDimension();

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
}
