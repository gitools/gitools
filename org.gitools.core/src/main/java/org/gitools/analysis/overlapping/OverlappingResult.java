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
package org.gitools.analysis.overlapping;

import org.gitools.core.matrix.model.matrix.element.LayerDef;

public class OverlappingResult {

    private int columnCount;

    private int rowCount;

    private int bothCount;

    private double columnOnlyProp;
    private double rowOnlyProp;

    private double columnIntersectionProp;
    private double rowIntersectionProp;

    private double jaccardIndex;

    private double maxIntersectionProp;

    public OverlappingResult() {
    }

    public OverlappingResult(int rowCount, int columnCount, int bothCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.bothCount = bothCount;
        calculateProportions();
    }

    @LayerDef(id = "column-count", name = "Column count", description = "Number of positive events in column condintion")
    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnPositiveCount) {
        this.columnCount = columnPositiveCount;
    }

    @LayerDef(id = "row-count", name = "Row count", description = "Number of positive events in row condintion")
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowPositiveCount) {
        this.rowCount = rowPositiveCount;
    }

    @LayerDef(id = "both-count", name = "Both count", description = "Number of positive events in both condintions")
    public int getBothCount() {
        return bothCount;
    }

    public void setBothCount(int bothPositiveCount) {
        this.bothCount = bothPositiveCount;
    }

    @LayerDef(id = "column-only-prop", name = "Column only proportion", description = "Proportion of events only in column condition out of the union")
    public double getColumnOnlyProp() {
        return columnOnlyProp;
    }

    public void setColumnOnlyProp(double columnOnlyProp) {
        this.columnOnlyProp = columnOnlyProp;
    }

    @LayerDef(id = "row-only-prop", name = "Row only proportion", description = "Proportion of events only in row condition out of the union")
    public double getRowOnlyProp() {
        return rowOnlyProp;
    }

    public void setRowOnlyProp(double rowOnlyProp) {
        this.rowOnlyProp = rowOnlyProp;
    }

    @LayerDef(id = "column-intersection-prop", name = "Column intersection proportion", description = "Proportion of shared events in column condition")
    public double getColumnIntersectionProp() {
        return columnIntersectionProp;
    }

    public void setColumnIntersectionProp(double columnIntersectionProp) {
        this.columnIntersectionProp = columnIntersectionProp;
    }

    @LayerDef(id = "row-intersection-prop", name = "Row intersection proportion", description = "Proportion of shared events in row condition")
    public double getRowIntersectionProp() {
        return rowIntersectionProp;
    }

    public void setRowIntersectionProp(double rowIntersectionProp) {
        this.rowIntersectionProp = rowIntersectionProp;
    }

    @LayerDef(id = "max-intersection-prop", name = "Maximum intersection proportion", description = "Maximum proportion of shared events")
    public double getMaxIntersectionProp() {
        return maxIntersectionProp;
    }

    public void setMaxIntersectionProp(double maxIntersectionProp) {
        this.maxIntersectionProp = maxIntersectionProp;
    }

    @LayerDef(id = "jaccard-index", name = "Jaccard index", description = "Proportion of shared events in both conditions")
    public double getJaccardIndex() {
        return jaccardIndex;
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public void setJaccardIndex(double jaccardIndex) {
        this.jaccardIndex = jaccardIndex;
    }

    final void calculateProportions() {
        double union = columnCount + rowCount - bothCount;

        double minCount = (double) Math.min(columnCount, rowCount);

        columnOnlyProp = columnCount / union;
        rowOnlyProp = rowCount / union;

        columnIntersectionProp = columnCount != 0 ? bothCount / (double) columnCount : 0.0;
        rowIntersectionProp = rowCount != 0 ? bothCount / (double) rowCount : 0.0;

        maxIntersectionProp = minCount != 0 ? bothCount / minCount : 0.0;

        jaccardIndex = union != 0 ? bothCount / union : 0.0;
    }
}
