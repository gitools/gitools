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

	private int ColumnPositiveCount;

	private int RowPositiveCount;

	private int BothPositiveCount;

	private double ColumnOnlyProp;
	private double RowOnlyProp;

	private double ColumnSharedProp;
	private double RowSharedProp;

	private double SharedProp;

	@AttributeDef(id = "column-count", name = "Column count", description = "Number of positive events in column condintion")
	public int getColumnPositiveCount() {
		return ColumnPositiveCount;
	}

	public void setColumnPositiveCount(int ColumnPositiveCount) {
		this.ColumnPositiveCount = ColumnPositiveCount;
	}

	@AttributeDef(id = "row-count", name = "Row count", description = "Number of positive events in row condintion")
	public int getRowPositiveCount() {
		return RowPositiveCount;
	}

	public void setRowPositiveCount(int RowPositiveCount) {
		this.RowPositiveCount = RowPositiveCount;
	}

	@AttributeDef(id = "both-count", name = "Both count", description = "Number of positive events in both condintions")
	public int getBothPositiveCount() {
		return BothPositiveCount;
	}

	public void setBothPositiveCount(int BothPositiveCount) {
		this.BothPositiveCount = BothPositiveCount;
	}

	@AttributeDef(id = "column-only-prop", name = "Column only proportion", description = "Proportion of events only in column condition")
	public double getColumnOnlyProp() {
		return ColumnOnlyProp;
	}

	public void setColumnOnlyProp(double ColumnOnlyProp) {
		this.ColumnOnlyProp = ColumnOnlyProp;
	}

	@AttributeDef(id = "row-only-prop", name = "Row only proportion", description = "Proportion of events only in row condition")
	public double getRowOnlyProp() {
		return RowOnlyProp;
	}

	public void setRowOnlyProp(double RowOnlyProp) {
		this.RowOnlyProp = RowOnlyProp;
	}

	@AttributeDef(id = "column-shared-prop", name = "Column shared proportion", description = "Proportion of shared events in column condition")
	public double getColumnSharedProp() {
		return ColumnSharedProp;
	}

	public void setColumnSharedProp(double ColumnSharedProp) {
		this.ColumnSharedProp = ColumnSharedProp;
	}

	@AttributeDef(id = "row-shared-prop", name = "Row shared proportion", description = "Proportion of shared events in row condition")
	public double getRowSharedProp() {
		return RowSharedProp;
	}

	public void setRowSharedProp(double RowSharedProp) {
		this.RowSharedProp = RowSharedProp;
	}

	@AttributeDef(id = "shared-prop", name = "Shared proportion", description = "Proportion of shared events in both conditions")
	public double getSharedProp() {
		return SharedProp;
	}

	public void setSharedProp(double SharedProp) {
		this.SharedProp = SharedProp;
	}
}
