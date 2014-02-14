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
package org.gitools.matrix.model;

import org.gitools.api.matrix.ILayerAdapter;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.iterable.AbstractSourceIterable;


public class AdapterSourceIterable<T> extends AbstractSourceIterable<T> {

    private ILayerAdapter<T> adapter;

    public AdapterSourceIterable(IMatrixPosition matrixPosition, MatrixDimensionKey dimensionKey, ILayerAdapter<T> adapter) {
        super(matrixPosition, dimensionKey);

        this.adapter = adapter;
    }

    @Override
    protected T getValue(IMatrixPosition position) {
        return adapter.get(getMatrix(), position);
    }
}
