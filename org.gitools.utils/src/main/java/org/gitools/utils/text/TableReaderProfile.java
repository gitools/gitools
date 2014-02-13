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

public class TableReaderProfile extends ReaderProfile {

    int[] heatmapColumns;
    int[] heatmapRows;
    String fieldGlue;

    public TableReaderProfile() {
        super();
        this.name = "deafultTable";
        this.heatmapColumns = new int[]{0};
        this.heatmapRows = new int[]{1};
        this.fieldGlue = "-";
        this.layout = TABLE;
    }

    /**
     * Which columns in the flat text are mapped as heatmap column id
     */
    public int[] getHeatmapColumns() {
        return heatmapColumns;
    }

    /**
     * @param heatmapColumns: indices of columns mapped to heatmap row ids
     */
    public void setHeatmapColumns(int[] heatmapColumns) {
        this.heatmapColumns = heatmapColumns;
    }

    /**
     * Which columns in the flat text are mapped as heatmap row id
     */
    public int[] getHeatmapRows() {
        return heatmapRows;
    }

    /**
     * @param heatmapRows indices of columns mapped to heatmap row ids
     */
    public void setHeatmapRows(int[] heatmapRows) {
        this.heatmapRows = heatmapRows;
    }

    /**
     * If multiple values are used as Column/Row ids, this character will unite the fields
     */
    public String getFieldGlue() {
        return fieldGlue;
    }

    /**
     * @param fieldGlue String to glue id fields together.
     */
    public void setFieldGlue(String fieldGlue) {
        this.fieldGlue = fieldGlue;
    }
}
