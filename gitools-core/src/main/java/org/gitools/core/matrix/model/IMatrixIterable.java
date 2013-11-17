package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

public interface IMatrixIterable<T> extends Iterable<T> {

    IMatrixIterable<T> from(String fromIdentifier);

    IMatrixIterable<T> to(String toIdentifier);

    IMatrixIterable<T> between(String fromIdentifier, String toIdentifier);

    IMatrixIterable<T> filter(Iterable<String> identifiers);

    IMatrixIterable<T> filter(IMatrixPredicate<T> filter);

    <F> IMatrixIterable<F> transform(IMatrixFunction<F, T> function);

    IMatrixIterable<T> monitor(IProgressMonitor monitor, String title);

    void store(IMatrix output, IMatrixPositionMapping mapping, LayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixPositionMapping mapping, IMatrixLayer<T> layer);

    void store(IMatrix output, LayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixLayer<T> layer);


}
