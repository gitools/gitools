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
package org.gitools.matrix;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.IMatrixViewDimension;
import org.gitools.matrix.model.IMatrixViewLayers;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;

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
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mv.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mv.removePropertyChangeListener(listener);
    }

    @Override
    public IMatrixViewDimension getRows() {
        return mv.getColumns();
    }

    @Override
    public IMatrixViewDimension getColumns() {
        return mv.getRows();
    }

    @Override
    public Object getCellValue(int row, int column, int layer) {
        return mv.getCellValue(column, row, layer);
    }

    @Override
    public void setCellValue(int row, int column, int layer, Object value) {
        mv.setCellValue(column, row, layer, value);
    }

    @Override
    public IMatrixViewLayers getLayers() {
        return mv.getLayers();
    }

    @Override
    public void detach() {
        mv.detach();
    }

}
