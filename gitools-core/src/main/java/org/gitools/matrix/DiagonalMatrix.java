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

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.persistence.IResourceLocator;

public class DiagonalMatrix implements IMatrix
{

    private final IMatrix m;

    private IResourceLocator locator;

    public DiagonalMatrix(IMatrix matrix)
    {
        this.m = matrix;
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    @Override
    public IMatrixDimension getRows()
    {
        return m.getRows();
    }

    @Override
    public IMatrixDimension getColumns()
    {
        return m.getColumns();
    }

    @Override
    public boolean isEmpty(int row, int column)
    {
        return m.isEmpty(row, column);
    }



    @Override
    public Object getCellValue(int row, int column, int layer)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }
        return m.getCellValue(row, column, layer);
    }

    @Override
    public void setCellValue(int row, int column, int layer, Object value)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }

        m.setCellValue(row, column, layer, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return m.getCellAdapter();
    }

    @Override
    public IMatrixLayers getLayers()
    {
        return m.getLayers();
    }

    @Override
    public void detach()
    {
        m.detach();
    }

}
