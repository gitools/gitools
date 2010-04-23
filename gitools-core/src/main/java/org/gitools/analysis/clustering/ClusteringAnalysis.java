/*
 *  Copyright 2010 xrafael.
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

package org.gitools.analysis.clustering;

import java.util.Properties;
import org.gitools.matrix.model.IMatrixView;


public class ClusteringAnalysis {
	
	private String indexData;

	private boolean applyToRows;

	private boolean applyToColumns;

	private boolean applyToRowsColumns;

	private Properties params;

	private IMatrixView data;

	private ClusteringResult results;

	public ClusteringResult getResults() {
		return results;
	}

	public void setResults(ClusteringResult results) {
		this.results = results;
	}

	public boolean isApplyToRowsColumns() {
		return applyToRowsColumns;
	}

	public void setApplyToRowsColumns(boolean applyToRowsColumns) {
		this.applyToRowsColumns = applyToRowsColumns;
	}

	public IMatrixView getData() {
		return data;
	}

	public void setData(IMatrixView data) {
		this.data = data;
	}

	public boolean isApplyToColumns() {
		return applyToColumns;
	}

	public void setApplyToColumns(boolean applyToColumns) {
		this.applyToColumns = applyToColumns;
	}

	public boolean isApplyToRows() {
		return applyToRows;
	}

	public void setApplyToRows(boolean applyToRows) {
		this.applyToRows = applyToRows;
	}

	public String getIndexData() {
		return indexData;
	}

	public void setIndexData(String indexData) {
		this.indexData = indexData;
	}

	public Properties getParams() {
		return params;
	}

	public void setParams(Properties params) {
		this.params = params;
	}
	
}
