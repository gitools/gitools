/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.analysis.combination;

import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.ResourceReference;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CombinationAnalysis extends Analysis {

	protected String sizeAttrName;

	protected String pvalueAttrName;

	protected boolean transposeData;

	/** Modules */
	protected ResourceReference<ModuleMap> groupsMap;

	/** Data */
	protected ResourceReference<IMatrix> data;

	/** Results */
	protected ResourceReference<IMatrix> results;

	public CombinationAnalysis() {
		this.transposeData = false;
	}

	public String getSizeAttrName() {
		return sizeAttrName;
	}

	public void setSizeAttrName(String sizeAttrName) {
		this.sizeAttrName = sizeAttrName;
	}

	public String getPvalueAttrName() {
		return pvalueAttrName;
	}

	public void setPvalueAttrName(String pvalueAttrName) {
		this.pvalueAttrName = pvalueAttrName;
	}

	public boolean isTransposeData() {
		return transposeData;
	}

	public void setTransposeData(boolean transposeData) {
		this.transposeData = transposeData;
	}

	public ResourceReference<ModuleMap> getGroupsMap() {
		return groupsMap;
	}

	public void setGroupsMap(ResourceReference<ModuleMap> groupsMap) {
		this.groupsMap = groupsMap;
	}

	public ResourceReference<IMatrix> getData() {
		return data;
	}

	public void setData(ResourceReference<IMatrix> data) {
		this.data = data;
	}

	public ResourceReference<IMatrix> getResults() {
		return results;
	}

	public void setResults(ResourceReference<IMatrix> results) {
		this.results = results;
	}
}
