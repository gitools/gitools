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
package org.gitools.core.heatmap;

import org.gitools.core.matrix.model.*;
import org.gitools.core.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

public class HeatmapAnnotatedMatrixView implements IMatrixView {

    @NotNull
    private final Heatmap hm;

    private IResourceLocator locator;

    public HeatmapAnnotatedMatrixView(@NotNull Heatmap hm) {
        this.hm = hm;
    }

    @Override
    public IResourceLocator getLocator() {
        return locator;
    }

    @Override
    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    @Override
    public IMatrix getContents() {
        return hm.getContents();
    }

    @Override
    public IMatrixViewDimension getRows() {
        return hm.getRows();
    }

    @Override
    public IMatrixViewDimension getColumns() {
        return hm.getColumns();
    }

    @Override
    public Object getValue(int row, int column, int layer) {
        return hm.getValue(row, column, layer);
    }

    @Override
    public Object getValue(IMatrixPosition position) {
        return hm.getValue(position);
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        hm.setValue(row, column, layer, value);
    }

    @Override
    public void setValue(IMatrixPosition position, Object value) {
        hm.setValue(position, value);
    }

    @Override
    public IMatrixIterator newIterator() {
        return hm.newIterator();
    }

    @Override
    public IMatrixViewLayers getLayers() {
        return hm.getLayers();
    }

    @Override
    public void detach() {
        hm.detach();
    }

}
