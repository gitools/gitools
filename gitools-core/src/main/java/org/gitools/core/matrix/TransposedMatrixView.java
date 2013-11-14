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

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.*;
import org.gitools.core.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransposedMatrixView implements IMatrixView {

    private IMatrixView mv;

    private IResourceLocator locator;

    public TransposedMatrixView() {
    }

    public TransposedMatrixView(IMatrixView mv) {
        this.mv = mv;
    }

    public TransposedMatrixView(@NotNull IMatrix mv) {
        setMatrix(mv);
    }

    public final void setMatrix(@NotNull IMatrix matrix) {
        this.mv = matrix instanceof IMatrixView ? (IMatrixView) matrix : new Heatmap(matrix);
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    @Override
    public IMatrix getContents() {
        return new TransposedMatrix(mv.getContents());
    }

    @Override
    public Object getValue(int row, int column, int layer) {
        return mv.getValue(column, row, layer);
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, IMatrixPosition position) {
        return mv.get(layer, position);
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return mv.get(layer, identifiers);
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        mv.setValue(column, row, layer, value);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, IMatrixPosition position) {
        mv.set(layer, value, position);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        mv.set(layer, value, identifiers);
    }

    @Override
    public List<MatrixDimension> getDimensions() {
        return mv.getDimensions();
    }

    @Override
    public IMatrixViewDimension getIdentifiers(MatrixDimension dimension) {
        return mv.getIdentifiers(dimension);
    }

    @Override
    public IMatrixViewLayers getLayers() {
        return mv.getLayers();
    }

    @Override
    public void detach() {
        mv.detach();
    }

    @Override
    public IMatrixViewDimension getRows() {
        return mv.getRows();
    }

    @Override
    public IMatrixViewDimension getColumns() {
        return mv.getColumns();
    }

}
