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
import org.gitools.matrix.model.element.ILayerDescriptor;
import org.gitools.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class BaseMatrix extends Resource implements IMatrix, Serializable
{

    private static final long serialVersionUID = 4021765485781500318L;

    ObjectMatrix1D rows;
    ObjectMatrix1D columns;

    IElementAdapter cellAdapter;

    BaseMatrix()
    {
        this("", ObjectFactory1D.dense.make(0), ObjectFactory1D.dense.make(0), null);
    }

    BaseMatrix(String title, ObjectMatrix1D rows, ObjectMatrix1D columns, IElementAdapter cellAdapter)
    {

        this.title = title;

        this.rows = rows;
        this.columns = columns;

        this.cellAdapter = cellAdapter;
    }

    public void make(int numRows, int numColumns)
    {
        rows = ObjectFactory1D.dense.make(numRows);
        columns = ObjectFactory1D.dense.make(numColumns);
        makeCells(numRows, numColumns);
    }

    public abstract void makeCells(int numRows, int numColumns);

    // rows

    public ObjectMatrix1D getInternalRows()
    {
        return rows;
    }

    @NotNull
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

    @NotNull
    public String internalRowLabel(int index)
    {
        return (String) rows.get(index);
    }

    public void setRow(int index, Object row)
    {
        rows.set(index, row);
    }

    // columns

    public ObjectMatrix1D getInternalColumns()
    {
        return columns;
    }

    @NotNull
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

    @NotNull
    public String internalColumnLabel(int index)
    {
        return (String) columns.get(index);
    }

    public void setColumn(int index, Object column)
    {
        columns.set(index, column);
    }

    // adapters

    public IElementAdapter getObjectCellAdapter()
    {
        return cellAdapter;
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return getObjectCellAdapter();
    }

    public void setObjectCellAdapter(IElementAdapter cellAdapter)
    {
        this.cellAdapter = cellAdapter;
    }

    // attributes
    @Override
    public IMatrixLayers<? extends ILayerDescriptor> getLayers()
    {
        return cellAdapter.getProperties();
    }

    public int internalRowIndex(String label)
    {
        Object[] rows = getInternalRows().toArray();
        for (int i = 0; i < rows.length; i++)
        {
            String s = (String) rows[i];
            if (s.equals(label))
            {
                return i;
            }
        }
        return -1;
    }

    public int internalColumnIndex(String label)
    {
        Object[] cols = getInternalColumns().toArray();
        for (int i = 0; i < cols.length; i++)
        {
            String s = (String) cols[i];
            if (s.equals(label))
            {
                return i;
            }
        }
        return -1;
    }

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumns().size()).append(" columns, ");
        sb.append(getRows().size()).append(" rows");
        return sb.toString();
    }

    @Override
    public void detach()
    {
        // Method to Override
    }

    @Override
    public IMatrixDimension getRows()
    {
        return new IMatrixDimension()
        {
            @Override
            public int size()
            {
                return BaseMatrix.this.internalRowCount();
            }

            @Override
            public String getLabel(int index)
            {
                return BaseMatrix.this.internalRowLabel(index);
            }

            @Override
            public int getIndex(String label)
            {
                return BaseMatrix.this.internalRowIndex(label);
            }
        };
    }

    @Override
    public IMatrixDimension getColumns()
    {
        return new IMatrixDimension()
        {
            @Override
            public int size()
            {
                return BaseMatrix.this.internalColumnCount();
            }

            @Override
            public String getLabel(int index)
            {
                return BaseMatrix.this.internalColumnLabel(index);
            }

            @Override
            public int getIndex(String label)
            {
                return BaseMatrix.this.internalColumnIndex(label);
            }
        };
    }

    @Override
    public abstract boolean isEmpty(int row, int column);

    protected abstract int internalRowCount();

    protected abstract int internalColumnCount();
}
