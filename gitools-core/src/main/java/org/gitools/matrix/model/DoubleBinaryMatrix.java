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

import cern.colt.bitvector.BitMatrix;
import cern.colt.matrix.ObjectFactory1D;
import org.gitools.matrix.model.element.DoubleElementAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoubleBinaryMatrix extends BaseMatrix
{

    private BitMatrix cells;
    private BitMatrix cellsNan;

    public DoubleBinaryMatrix()
    {
        this("", new String[0], new String[0], new BitMatrix(0, 0));
    }

    public DoubleBinaryMatrix(String title, String[] colNames, String[] rowNames, BitMatrix cells)
    {

        super(title, ObjectFactory1D.dense.make(rowNames), ObjectFactory1D.dense.make(colNames), new DoubleElementAdapter());

        this.cells = cells;
    }

    public DoubleBinaryMatrix(String title, @NotNull String[] colNames, @NotNull String[] rowNames)
    {

        super(title, ObjectFactory1D.dense.make(rowNames), ObjectFactory1D.dense.make(colNames), new DoubleElementAdapter());

        makeCells(rowNames.length, colNames.length);
    }

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

    @Override
    public Object getCell(int row, int column)
    {
        if (cellsNan.getQuick(column, row))
        {
            return Double.NaN;
        }
        else
        {
            return cells.getQuick(column, row) ? 1.0 : 0.0;
        }
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return getCell(row, column);
    }

    @Override
    public void setCellValue(int row, int column, int index, @Nullable Object value)
    {
        if (value != null)
        {
            cells.putQuick(column, row, ((Double) value) == 1.0);
            cellsNan.putQuick(column, row, Double.isNaN((Double) value));
        }
        else // FIXME null and NaN are different things
        {
            cellsNan.putQuick(column, row, true);
        }
    }

    @Override
    public void makeCells(int rows, int columns)
    {
        cells = new BitMatrix(columns, rows);
        cells.clear();

        cellsNan = new BitMatrix(columns, rows);
        cellsNan.clear();

        if (this.rows == null || this.rows.cardinality() != rows)
        {
            setRows(ObjectFactory1D.dense.make(rows));
        }

        if (this.columns == null || this.columns.cardinality() != columns)
        {
            setColumns(ObjectFactory1D.dense.make(columns));
        }
    }
}
