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
package org.gitools.matrix.model.matrix;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;
import org.gitools.matrix.model.matrix.element.DoubleElementAdapter;
import org.jetbrains.annotations.Nullable;

public class DoubleMatrix extends BaseMatrix {

    private static final long serialVersionUID = -710485141066995079L;

    private DoubleMatrix2D cells;

    public DoubleMatrix() {
        this("", new String[0], new String[0], DoubleFactory2D.dense.make(0, 0));
    }

    public DoubleMatrix(String title, String[] colNames, String[] rowNames, DoubleMatrix2D cells) {

        super(title, ObjectFactory1D.dense.make(rowNames), ObjectFactory1D.dense.make(colNames), new DoubleElementAdapter());

        this.cells = cells;
    }

    protected int internalColumnCount() {
        return columns.cardinality();
    }

    protected int internalRowCount() {
        return rows.cardinality();
    }

    public Object getCell(int row, int column) {
        return cells.get(row, column);
    }

    @Override
    public Object getValue(int[] position, int layer) {
        return cells.get(position[0], position[1]);
    }

    @Override
    public void setValue(int[] position, int layer, @Nullable Object value) {
        cells.set(position[0], position[1], value != null ? (Double) value : Double.NaN);
    }

    @Override
    public void makeCells(int rows, int columns) {
        this.cells = DoubleFactory2D.dense.make(rows, columns);
    }
}
