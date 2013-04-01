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
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.gitools.matrix.model.element.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

//TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public class ObjectMatrix extends BaseMatrix
{

    private static final long serialVersionUID = 4077172838934816719L;

    protected ObjectMatrix2D cells;

    public ObjectMatrix()
    {
        super();
    }

    public ObjectMatrix(
            String title,
            ObjectMatrix1D rows,
            ObjectMatrix1D columns,
            ObjectMatrix2D cells,
            IElementAdapter cellAdapter)
    {

        super(title, rows, columns, cellAdapter);

        this.cells = cells;
    }

    public ObjectMatrix(
            String title,
            String[] rowNames,
            String[] columnNames,
            IElementAdapter cellAdapter)
    {


        super(title, ObjectFactory1D.dense.make(rowNames), ObjectFactory1D.dense.make(columnNames), cellAdapter);

        makeCells(rowNames.length, columnNames.length);
    }

    // rows and columns

    @Override
    public int getRowCount()
    {
        return cells.rows();
    }

    @Override
    public int getColumnCount()
    {
        return cells.columns();
    }

    // cells

    public ObjectMatrix2D getCells()
    {
        return cells;
    }

    public void setCells(ObjectMatrix2D cells)
    {
        this.cells = cells;
    }

    @Override
    public Object getCell(int row, int column)
    {
        return cells.get(row, column);
    }

    public void setCell(int row, int column, Object cell)
    {
        cells.set(row, column, cell);
    }

    @Override
    public Object getCellValue(int row, int column, int property)
    {
        return cellAdapter.getValue(getCell(row, column), property);
    }

    @Override
    public void setCellValue(int row, int column, int property, Object value)
    {
        cellAdapter.setValue(getCell(row, column), property, value);
    }

    public void makeCells()
    {
        makeCells(rows.size(), columns.size());
    }

    @Override
    public void makeCells(int rows, int columns)
    {
        cells = ObjectFactory2D.dense.make(rows, columns);
        if (cellAdapter != null)
        {

            IElementFactory elementFactory;
            if (cellAdapter instanceof BeanElementAdapter)
            {
                elementFactory = new BeanElementFactory(cellAdapter.getElementClass());
            }
            else
            {
                elementFactory = new ArrayElementFactory(cellAdapter.getPropertyCount());
            }

            for (int r = 0; r < rows; r++)
            {
                for (int c = 0; c < columns; c++)
                {
                    setCell(r, c, elementFactory.create());
                }
            }
        }
    }
}
