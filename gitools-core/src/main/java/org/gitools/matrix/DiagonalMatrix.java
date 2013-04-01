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
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;

import java.util.List;

public class DiagonalMatrix implements IMatrix
{

    private IMatrix m;

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
    public int getRowCount()
    {
        return m.getRowCount();
    }

    @Override
    public String getRowLabel(int index)
    {
        return m.getRowLabel(index);
    }

    @Override
    public int getColumnCount()
    {
        return m.getColumnCount();
    }

    @Override
    public String getColumnLabel(int index)
    {
        return m.getColumnLabel(index);
    }

    @Override
    public Object getCell(int row, int column)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }
        return m.getCell(row, column);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }
        return m.getCellValue(row, column, index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }
        return m.getCellValue(row, column, id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }

        m.setCellValue(row, column, index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        if (column < row)
        {
            int tmp = column;
            column = row;
            row = tmp;
        }
        m.setCellValue(row, column, id, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return m.getCellAdapter();
    }

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return m.getCellAttributes();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return m.getCellAttributeIndex(id);
    }

}
