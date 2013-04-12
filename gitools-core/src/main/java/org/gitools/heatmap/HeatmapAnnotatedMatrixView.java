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
package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.IMatrixViewDimension;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.util.List;

public class HeatmapAnnotatedMatrixView implements IMatrixView
{

    @NotNull
    private final Heatmap hm;

    private IResourceLocator locator;

    public HeatmapAnnotatedMatrixView(@NotNull Heatmap hm)
    {
        this.hm = hm;
    }

    @Override
    public IResourceLocator getLocator()
    {
        return locator;
    }

    @Override
    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    @Override
    public IMatrix getContents()
    {
        return hm.getContents();
    }

    @Override
    public int getSelectedLayer()
    {
        return hm.getSelectedLayer();
    }

    @Override
    public void setSelectedLayer(int index)
    {
        hm.setSelectedLayer(index);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        hm.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        hm.removePropertyChangeListener(listener);
    }

    @Override
    public IMatrixViewDimension getRows()
    {
        return hm.getRows();
    }

    @Override
    public IMatrixViewDimension getColumns()
    {
        return hm.getColumns();
    }

    @Override
    public Object getCell(int row, int column)
    {
        return hm.getCell(row, column);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return hm.getCellValue(row, column, index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return hm.getCellValue(row, column, id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        hm.setCellValue(row, column, index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        hm.setCellValue(row, column, id, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return hm.getCellAdapter();
    }

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return hm.getCellAttributes();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return hm.getCellAttributeIndex(id);
    }

    @Override
    public void detach()
    {
        hm.detach();
    }
}
