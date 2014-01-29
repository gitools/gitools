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
package org.gitools.api.matrix.position;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.ILayerAdapter;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;

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

    void store(IMatrix output, IMatrixPositionMapping mapping, ILayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixPositionMapping mapping, IMatrixLayer<T> layer);

    void store(IMatrix output, ILayerAdapter<T> layerAdapter);

    void store(IMatrix output, IMatrixLayer<T> layer);

    int count();


}
