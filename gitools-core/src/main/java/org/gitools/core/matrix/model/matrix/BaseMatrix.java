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
package org.gitools.core.matrix.model.matrix;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.matrix.element.AbstractElementAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class BaseMatrix extends AbstractMatrix implements Serializable {

    private static final long serialVersionUID = 4021765485781500318L;

    ObjectMatrix1D rows;
    ObjectMatrix1D columns;

    AbstractElementAdapter cellAdapter;

    BaseMatrix() {
        this("", ObjectFactory1D.dense.make(0), ObjectFactory1D.dense.make(0), null);
    }

    BaseMatrix(String title, ObjectMatrix1D rows, ObjectMatrix1D columns, AbstractElementAdapter cellAdapter) {

        this.title = title;

        this.rows = rows;
        this.columns = columns;

        this.cellAdapter = cellAdapter;
    }

    public void make(int numRows, int numColumns) {
        rows = ObjectFactory1D.dense.make(numRows);
        columns = ObjectFactory1D.dense.make(numColumns);
        makeCells(numRows, numColumns);
    }

    public abstract void makeCells(int numRows, int numColumns);

    public ObjectMatrix1D getInternalRows() {
        return rows;
    }

    @NotNull
    @Deprecated
    public String[] getRowStrings() {
        String[] a = new String[rows.size()];
        rows.toArray(a);
        return a;
    }

    public void setRows(ObjectMatrix1D rows) {
        this.rows = rows;
    }

    public void setRows(String[] names) {
        this.rows = ObjectFactory1D.dense.make(names);
    }

    @NotNull
    public String internalRowLabel(int index) {
        return (String) rows.get(index);
    }

    public void setRow(int index, Object row) {
        rows.set(index, row);
    }

    // columns

    public ObjectMatrix1D getInternalColumns() {
        return columns;
    }

    @NotNull
    @Deprecated
    public String[] getColumnStrings() {
        String[] a = new String[columns.size()];
        columns.toArray(a);
        return a;
    }

    public void setColumns(ObjectMatrix1D columns) {
        this.columns = columns;
    }

    public void setColumns(String[] names) {
        this.columns = ObjectFactory1D.dense.make(names);
    }

    @NotNull
    public String getLabel(int index) {
        return (String) columns.get(index);
    }

    public void setColumn(int index, Object column) {
        columns.set(index, column);
    }

    // adapters

    public AbstractElementAdapter getObjectCellAdapter() {
        return cellAdapter;
    }

    public void setObjectCellAdapter(AbstractElementAdapter cellAdapter) {
        this.cellAdapter = cellAdapter;
    }

    // attributes
    @Override
    public IMatrixLayers<? extends IMatrixLayer> getLayers() {
        return cellAdapter.getProperties();
    }

    public int internalRowIndex(String label) {
        Object[] rows = getInternalRows().toArray();
        for (int i = 0; i < rows.length; i++) {
            String s = (String) rows[i];
            if (s.equals(label)) {
                return i;
            }
        }
        return -1;
    }

    public int internalColumnIndex(String label) {
        Object[] cols = getInternalColumns().toArray();
        for (int i = 0; i < cols.length; i++) {
            String s = (String) cols[i];
            if (s.equals(label)) {
                return i;
            }
        }
        return -1;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumns().size()).append(" columns, ");
        sb.append(getRows().size()).append(" rows");
        return sb.toString();
    }

    @Override
    public void detach() {
        // Method to Override
    }

    @Override
    public IMatrixDimension getRows() {
        return new AbstractMatrixDimension("rows", 0) {

            @Override
            public int size() {
                return BaseMatrix.this.internalRowCount();
            }

            @Override
            public String getLabel(int index) {
                return BaseMatrix.this.internalRowLabel(index);
            }

            @Override
            public int getIndex(String label) {
                return BaseMatrix.this.internalRowIndex(label);
            }
        };
    }

    @Override
    public IMatrixDimension getColumns() {
        return new AbstractMatrixDimension("columns", 1) {

            @Override
            public int size() {
                return BaseMatrix.this.internalColumnCount();
            }

            @Override
            public String getLabel(int index) {
                return BaseMatrix.this.getLabel(index);
            }

            @Override
            public int getIndex(String label) {
                return BaseMatrix.this.internalColumnIndex(label);
            }
        };
    }

    protected abstract int internalRowCount();

    protected abstract int internalColumnCount();
}
