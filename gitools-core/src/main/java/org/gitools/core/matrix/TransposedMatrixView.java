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
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.gitools.core.matrix.model.IMatrixViewLayers;
import org.gitools.core.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

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
    public IMatrixViewDimension getRows() {
        return mv.getColumns();
    }

    @Override
    public IMatrixViewDimension getColumns() {
        return mv.getRows();
    }

    @Override
    public Object getValue(int row, int column, int layer) {
        return mv.getValue(column, row, layer);
    }

    @Override
    public Object getValue(int[] position, int layer) {
        return mv.getValue(position, layer);
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        mv.setValue(column, row, layer, value);
    }

    @Override
    public void setValue(int[] position, int layer, Object value) {
        mv.setValue(position, layer, value);
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
