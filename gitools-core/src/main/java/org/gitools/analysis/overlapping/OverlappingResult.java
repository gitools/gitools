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

	private double columnSharedProp;
	private double rowSharedProp;

	private double sharedProp;

	public OverlappingResult() {
	}

	public OverlappingResult(int columnCount, int rowCount, int bothCount) {
		this.columnCount = columnCount;
		this.rowCount = rowCount;
		this.bothCount = bothCount;
		calculateProportions();
	}

	@AttributeDef(id = "column-count", name = "Column count", description = "Number of positive events in column condintion")
	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int ColumnPositiveCount) {
		this.columnCount = ColumnPositiveCount;
	}

	@AttributeDef(id = "row-count", name = "Row count", description = "Number of positive events in row condintion")
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int RowPositiveCount) {
		this.rowCount = RowPositiveCount;
	}

	@AttributeDef(id = "both-count", name = "Both count", description = "Number of positive events in both condintions")
	public int getBothCount() {
		return bothCount;
	}

	public void setBothCount(int BothPositiveCount) {
		this.bothCount = BothPositiveCount;
	}

	@AttributeDef(id = "column-only-prop", name = "Column only proportion", description = "Proportion of events only in column condition")
	public double getColumnOnlyProp() {
		return columnOnlyProp;
	}

	public void setColumnOnlyProp(double ColumnOnlyProp) {
		this.columnOnlyProp = ColumnOnlyProp;
	}

	@AttributeDef(id = "row-only-prop", name = "Row only proportion", description = "Proportion of events only in row condition")
	public double getRowOnlyProp() {
		return rowOnlyProp;
	}

	public void setRowOnlyProp(double RowOnlyProp) {
		this.rowOnlyProp = RowOnlyProp;
	}

	@AttributeDef(id = "column-shared-prop", name = "Column shared proportion", description = "Proportion of shared events in column condition")
	public double getColumnSharedProp() {
		return columnSharedProp;
	}

	public void setColumnSharedProp(double ColumnSharedProp) {
		this.columnSharedProp = ColumnSharedProp;
	}

	@AttributeDef(id = "row-shared-prop", name = "Row shared proportion", description = "Proportion of shared events in row condition")
	public double getRowSharedProp() {
		return rowSharedProp;
	}

	public void setRowSharedProp(double RowSharedProp) {
		this.rowSharedProp = RowSharedProp;
	}

	@AttributeDef(id = "shared-prop", name = "Shared proportion", description = "Proportion of shared events in both conditions")
	public double getSharedProp() {
		return sharedProp;
	}

	public void setSharedProp(double SharedProp) {
		this.sharedProp = SharedProp;
	}
	
	public final void calculateProportions() {
		double union = columnCount + rowCount - bothCount;

		columnOnlyProp = columnCount / union;
		rowOnlyProp = rowCount / union;

		columnSharedProp = bothCount / (double) columnCount;
		rowSharedProp = bothCount / (double) rowCount;

		sharedProp = bothCount / union;
	}
}
