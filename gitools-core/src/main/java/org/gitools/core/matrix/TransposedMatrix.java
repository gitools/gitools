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
package org.gitools.core.matrix;

import org.gitools.core.matrix.model.*;
import org.gitools.core.persistence.IResourceLocator;

public class TransposedMatrix implements IMatrix {

    private IMatrix matrix;

    public TransposedMatrix(IMatrix contents) {
        this.matrix = contents;
    }

    @Override
    public IMatrixDimension getRows() {
        return matrix.getColumns();
    }

    @Override
    public IMatrixDimension getColumns() {
        return matrix.getRows();
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        matrix.setValue(row, column, layer, value);
    }

    @Override
    public void setValue(IMatrixPosition position, Object value) {
        matrix.setValue(position, value);
    }

    @Override
    public IMatrixIterator newIterator() {
        return matrix.newIterator();
    }

    @Override
    public IMatrixLayers getLayers() {
        return matrix.getLayers();
    }

    @Override
    public Object getValue(int row, int column, int layerIndex) {
        return matrix.getValue(row, column, layerIndex);
    }

    @Override
    public Object getValue(IMatrixPosition position) {
        return matrix.getValue(position);
    }

    @Override
    public void detach() {
        matrix.detach();
    }

    @Override
    public IResourceLocator getLocator() {
        return matrix.getLocator();
    }

    @Override
    public void setLocator(IResourceLocator locator) {
        matrix.setLocator(locator);
    }
}
