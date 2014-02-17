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


import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

public class MatrixReaderProfile extends ReaderProfile {
    int rowIdsPosition;
    int columnIdsPosition;

    private MatrixReaderProfile() {
        super();
        this.name = "defaultMatrix";
        this.rowIdsPosition = 0;
        this.columnIdsPosition = 0;
        this.layout = MATRIX;
    }

    public static MatrixReaderProfile fromProfile(ReaderProfile profile) {
        MatrixReaderProfile newProfile = new MatrixReaderProfile();
        newProfile.setCommentChar(profile.getCommentChar());
        newProfile.setIgnoredColumns(profile.getIgnoredColumns());
        newProfile.setSeparator(profile.getSeparator());
        newProfile.setSkipLines(profile.getSkipLines());
        return newProfile;
    }

    @Override
    public void validate(List<FileHeader> inFileHeaders) throws ReaderProfileValidationException {
        if (rowIdsPosition < 0 || rowIdsPosition >= inFileHeaders.size()) {
            throw new ReaderProfileValidationException("Matrix rows ids wrongly assigned: " + rowIdsPosition);
        }
        if (columnIdsPosition < 0) {
            throw new ReaderProfileValidationException("Matrix columns ids wrongly assigned: " + columnIdsPosition);
        }

        if (dataColumns.length == 0) {
            List<Integer> dataIndices = new ArrayList<>();
            List<Integer> ignored = Ints.asList(ignoredColumns);
            for (FileHeader h : inFileHeaders) {
                if (h.getPos() != columnIdsPosition && (!ignored.contains(h.getPos()))) {
                    dataIndices.add(h.getPos());
                }
            }
            dataColumns = Ints.toArray(dataIndices);
            if (dataColumns.length == 0) {
                throw new ReaderProfileValidationException(
                        "No data columns available (too many ignored columns?)"
                );
            }
        }
    }

    @Override
    public String getColId(String[] fields) {
        return null;
    }

    @Override
    public String getRowId(String[] fields) {
        return null;
    }

    @Override
    public String[] getDataFields(String[] fields) {
        return new String[0];
    }

    @Override
    public int getDataColumnsNumber() {
        return 0;
    }

    @Override
    public String[] getHeatmapHeaders() {
        return new String[0];
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
