/*
 *  Copyright 2010 cperez.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis.overlapping;

import org.gitools.matrix.model.element.AttributeDef;


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

	@AttributeDef(id = "column-count", name = "Column count", description = "Number of positive events in column condintion")
	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnPositiveCount) {
		this.columnCount = columnPositiveCount;
	}

	@AttributeDef(id = "row-count", name = "Row count", description = "Number of positive events in row condintion")
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowPositiveCount) {
		this.rowCount = rowPositiveCount;
	}

	@AttributeDef(id = "both-count", name = "Both count", description = "Number of positive events in both condintions")
	public int getBothCount() {
		return bothCount;
	}

	public void setBothCount(int bothPositiveCount) {
		this.bothCount = bothPositiveCount;
	}

	@AttributeDef(id = "column-only-prop", name = "Column only proportion", description = "Proportion of events only in column condition out of the union")
	public double getColumnOnlyProp() {
		return columnOnlyProp;
	}

	public void setColumnOnlyProp(double columnOnlyProp) {
		this.columnOnlyProp = columnOnlyProp;
	}

	@AttributeDef(id = "row-only-prop", name = "Row only proportion", description = "Proportion of events only in row condition out of the union")
	public double getRowOnlyProp() {
		return rowOnlyProp;
	}

	public void setRowOnlyProp(double rowOnlyProp) {
		this.rowOnlyProp = rowOnlyProp;
	}

	@AttributeDef(id = "column-intersection-prop", name = "Column intersection proportion", description = "Proportion of shared events in column condition")
	public double getColumnIntersectionProp() {
		return columnIntersectionProp;
	}

	public void setColumnIntersectionProp(double columnIntersectionProp) {
		this.columnIntersectionProp = columnIntersectionProp;
	}

	@AttributeDef(id = "row-intersection-prop", name = "Row intersection proportion", description = "Proportion of shared events in row condition")
	public double getRowIntersectionProp() {
		return rowIntersectionProp;
	}

	public void setRowIntersectionProp(double rowIntersectionProp) {
		this.rowIntersectionProp = rowIntersectionProp;
	}

	@AttributeDef(id = "max-intersection-prop", name = "Maximum intersection proportion", description = "Maximum proportion of shared events")
	public double getMaxIntersectionProp() {
		return maxIntersectionProp;
	}

	public void setMaxIntersectionProp(double maxIntersectionProp) {
		this.maxIntersectionProp = maxIntersectionProp;
	}

	@AttributeDef(id = "jaccard-index", name = "Jaccard index", description = "Proportion of shared events in both conditions")
	public double getJaccardIndex() {
		return jaccardIndex;
	}

	public void setJaccardIndex(double jaccardIndex) {
		this.jaccardIndex = jaccardIndex;
	}

	public final void calculateProportions() {
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
