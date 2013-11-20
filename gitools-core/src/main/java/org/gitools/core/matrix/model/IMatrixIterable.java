package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;
import java.util.Set;

public interface IMatrixIterable<T> extends Iterable<T> {

    int size();

    IMatrixPosition getPosition();

    IMatrixDimension getIterateDimension();

    IMatrixIterable<T> from(String fromIdentifier);

    IMatrixIterable<T> to(String toIdentifier);

    IMatrixIterable<T> between(String fromIdentifier, String toIdentifier);

    IMatrixIterable<T> filter(Set<String> identifiers);

    IMatrixIterable<T> filter(IMatrixPredicate<T> filter);

    <F> IMatrixIterable<F> transform(IMatrixFunction<F, T> function);

    IMatrixIterable<T> sort();

    IMatrixIterable<T> sort(Comparator<T> comparator);

    IMatrixIterable<T> monitor(IProgressMonitor monitor, String title);

    IMatrixIterable<T> monitor(IProgressMonitor monitor);

    void store(IMatrix output, IMatrixPositionMapping mapping, LayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixPositionMapping mapping, IMatrixLayer<T> layer);

    void store(IMatrix output, LayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixLayer<T> layer);

    int count();


}
