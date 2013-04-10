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
package org.gitools.matrix.model.compressmatrix;


import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Artifact;
import org.gitools.persistence.IResourceLocator;

import java.util.List;

/**
 * The type Compress matrix.
 *
 * This format keep the rows compressed at memory, and has a dynamic cache that can expand or
 * contract depending on the user free memory.
 */
public class CompressMatrix extends Artifact implements IMatrix
{
    private IResourceLocator locator;

    private CompressDimension rows;
    private CompressDimension columns;

    private CompressElementAdapter elementAdapter;

    /**
     * Instantiates a new Compress matrix.
     *
     * @param rows the rows
     * @param columns the columns
     * @param elementAdapter the element adapter
     */
    public CompressMatrix(CompressDimension rows, CompressDimension columns, CompressElementAdapter elementAdapter)
    {
        this.rows = rows;
        this.columns = columns;
        this.elementAdapter = elementAdapter;
    }

    @Override
    public int getRowCount()
    {
        return rows.size();
    }

    @Override
    public String getRowLabel(int index)
    {
        return rows.getLabel(index);
    }

    @Override
    public int getRowIndex(String label)
    {
        return rows.getIndex(label);
    }

    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    @Override
    public String getColumnLabel(int index)
    {
        return columns.getLabel(index);
    }

    @Override
    public int getColumnIndex(String label)
    {
        return columns.getIndex(label);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return new int[] { row, column };
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return elementAdapter.getValue(getCell(row, column), index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return getCellValue(row, column, getCellAttributeIndex(id));
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        throw new UnsupportedOperationException("Read only matrix");
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        throw new UnsupportedOperationException("Read only matrix");
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return elementAdapter;
    }


    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return elementAdapter.getProperties();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return elementAdapter.getPropertyIndex(id);
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
}
