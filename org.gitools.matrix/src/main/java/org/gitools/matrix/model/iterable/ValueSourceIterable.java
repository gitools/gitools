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

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.IMatrixPosition;

public class ValueSourceIterable<T> extends AbstractSourceIterable<T> {

    private IMatrixLayer<T> layer;

    public ValueSourceIterable(IMatrixPosition position, MatrixDimensionKey iterateDimension, IMatrixLayer<T> layer) {
        super(position, iterateDimension);

        this.layer = layer;
    }

    @Override
    protected T getValue(IMatrixPosition position) {
        return getMatrix().get(layer, position);
    }
}
