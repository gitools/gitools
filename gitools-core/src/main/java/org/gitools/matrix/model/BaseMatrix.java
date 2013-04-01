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
package org.gitools.matrix.model;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Artifact;
import org.gitools.persistence.IResourceLocator;

import java.io.Serializable;
import java.util.List;

public abstract class BaseMatrix extends Artifact implements IMatrix, Serializable
{

    private static final long serialVersionUID = 4021765485781500318L;

    private IResourceLocator locator;

    protected ObjectMatrix1D rows;
    protected ObjectMatrix1D columns;

    protected IElementAdapter cellAdapter;

    public BaseMatrix()
    {
        this(
                "",
                ObjectFactory1D.dense.make(0),
                ObjectFactory1D.dense.make(0),
                null);
    }

    public BaseMatrix(
            String title,
            ObjectMatrix1D rows,
            ObjectMatrix1D columns,
            IElementAdapter cellAdapter)
    {

        this.title = title;

        this.rows = rows;
        this.columns = columns;

        this.cellAdapter = cellAdapter;
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    public void make(int numRows, int numColumns)
    {
        rows = ObjectFactory1D.dense.make(numRows);
        columns = ObjectFactory1D.dense.make(numColumns);
        makeCells(numRows, numColumns);
    }

    public abstract void makeCells(int numRows, int numColumns);

    // rows

    public ObjectMatrix1D getRows()
    {
        return rows;
    }

    @Deprecated
    public String[] getRowStrings()
    {
        String[] a = new String[rows.size()];
        rows.toArray(a);
        return a;
    }

    public void setRows(ObjectMatrix1D rows)
    {
        this.rows = rows;
    }

    public void setRows(String[] names)
    {
        this.rows = ObjectFactory1D.dense.make(names);
    }

    public Object getRow(int index)
    {
        return rows.get(index);
    }

    @Deprecated // Use getRowLabel() instead
    public String getRowString(int index)
    {
        return (String) rows.get(index);
    }

    @Override
    public String getRowLabel(int index)
    {
        return (String) rows.get(index);
    }

    public void setRow(int index, Object row)
    {
        rows.set(index, row);
    }

    // columns

    public ObjectMatrix1D getColumns()
    {
        return columns;
    }

    @Deprecated
    public String[] getColumnStrings()
    {
        String[] a = new String[columns.size()];
        columns.toArray(a);
        return a;
    }

    public void setColumns(ObjectMatrix1D columns)
    {
        this.columns = columns;
    }

    public void setColumns(String[] names)
    {
        this.columns = ObjectFactory1D.dense.make(names);
    }

    public Object getColumn(int index)
    {
        return columns.get(index);
    }

    @Deprecated // Use getColumnLabel() instead
    public String getColumnString(int index)
    {
        return (String) columns.get(index);
    }

    @Override
    public String getColumnLabel(int index)
    {
        return (String) columns.get(index);
    }

    public void setColumn(int index, Object column)
    {
        columns.set(index, column);
    }

    // cells

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return getCellValue(row, column, getCellAttributeIndex(id));
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        setCellValue(row, column, getCellAttributeIndex(id), value);
    }

    // adapters

    @Override
    public IElementAdapter getCellAdapter()
    {
        return cellAdapter;
    }

    public void setCellAdapter(IElementAdapter cellAdapter)
    {
        this.cellAdapter = cellAdapter;
    }

    // attributes

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return cellAdapter.getProperties();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        Integer index = cellAdapter.getPropertyIndex(id);
        if (index == null)
        {
            throw new RuntimeException("There isn't any property with id: " + id);
        }

        return index.intValue();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumnCount()).append(" columns, ");
        sb.append(getRowCount()).append(" rows");
        return sb.toString();
    }
}
