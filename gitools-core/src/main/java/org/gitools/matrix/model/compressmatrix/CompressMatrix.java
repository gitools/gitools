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
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.model.Resource;

/**
 * The type Compress matrix.
 * <p/>
 * This format keep the rows compressed at memory, and has a dynamic cache that can expand or
 * contract depending on the user free memory.
 */
public class CompressMatrix extends Resource implements IMatrix {
    private CompressDimension rows;
    private CompressDimension columns;

    private CompressElementAdapter elementAdapter;

    /**
     * Instantiates a new Compress matrix.
     *
     * @param rows           the rows
     * @param columns        the columns
     * @param elementAdapter the element adapter
     */
    public CompressMatrix(CompressDimension rows, CompressDimension columns, CompressElementAdapter elementAdapter) {
        this.rows = rows;
        this.columns = columns;
        this.elementAdapter = elementAdapter;
    }

    @Override
    public IMatrixDimension getColumns() {
        return columns;
    }

    @Override
    public boolean isEmpty(int row, int column) {
        return elementAdapter.isEmpty(row, column);
    }

    @Override
    public IMatrixDimension getRows() {
        return rows;
    }

    @Override
    public Object getCellValue(int row, int column, int layer) {
        return elementAdapter.getValue(new int[]{row, column}, layer);
    }

    @Override
    public void setCellValue(int row, int column, int layer, Object value) {
        throw new UnsupportedOperationException("Read only matrix");
    }

    @Override
    public IMatrixLayers getLayers() {
        return elementAdapter.getProperties();
    }

    @Override
    public void detach() {
        if (elementAdapter != null) {
            elementAdapter.detach();
        }
    }

}
