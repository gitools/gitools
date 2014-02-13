/*
 * #%L
 * org.gitools.utils
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.utils.text;


public class MatrixReaderProfile extends ReaderProfile {
    int rowIdsPosition;
    int columnIdsPosition;

    public MatrixReaderProfile() {
        super();
        this.name = "defaultMatrix";
        this.rowIdsPosition = 0;
        this.columnIdsPosition = 0;
        this.layout = MATRIX;
    }

    public int getColumnIdsPosition() {
        return columnIdsPosition;
    }

    /**
     * @param columnIdsPosition Index of text file row holding the column Ids
     */
    public void setColumnIdsPosition(int columnIdsPosition) {
        this.columnIdsPosition = columnIdsPosition;
    }

    public int getRowIdsPosition() {
        return rowIdsPosition;
    }

    /**
     * @param rowIdsPosition Index of text file column holding the row Ids
     */
    public void setRowIdsPosition(int rowIdsPosition) {
        this.rowIdsPosition = rowIdsPosition;
    }
}
