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

package org.gitools.analysis.overlapping;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.xml.adapter.CutoffCmpXmlAdapter;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ResourceRef;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OverlappingAnalysis extends Analysis {

	/** If different from null then replace NaN values by a number */
	protected Double replaceNanValue;

	/** If true then compare rows instead of columns */
	protected boolean transposeData;

	/** Data attribute name */
	protected String attributeName;

	/** Data binary cutoff enabled */
	protected boolean binaryCutoffEnabled;

	/** Data binary cutoff comparator */
	@XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
	protected CutoffCmp binaryCutoffCmp;

	/** Data binary cutoff value */
	protected Double binaryCutoffValue;

	/** Data source file */
	@XmlElement(name = "sourceData")
	protected ResourceRef sourceDataResource;

	/** Filtered data file */
	@XmlElement(name = "filteredData")
	protected ResourceRef filteredDataResource;

	/** Data */
	@XmlTransient
	protected IMatrix data;

	/** Cell Results file */
	@XmlElement(name = "cellResults")
	protected ResourceRef cellResultsResource;

	/** Results */
	@XmlTransient
	protected IMatrix cellResults;

	public OverlappingAnalysis() {
		this.transposeData = false;
	}

	public Double getReplaceNanValue() {
		return replaceNanValue;
	}

	public void setReplaceNanValue(Double replaceNanValue) {
		this.replaceNanValue = replaceNanValue;
	}

	public boolean isTransposeData() {
		return transposeData;
	}

	public void setTransposeData(boolean transposeData) {
		this.transposeData = transposeData;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean isBinaryCutoffEnabled() {
		return binaryCutoffEnabled;
	}

	public void setBinaryCutoffEnabled(boolean binaryCutoffEnabled) {
		this.binaryCutoffEnabled = binaryCutoffEnabled;
	}

	public CutoffCmp getBinaryCutoffCmp() {
		return binaryCutoffCmp;
	}

	public void setBinaryCutoffCmp(CutoffCmp binaryCutoffCmp) {
		this.binaryCutoffCmp = binaryCutoffCmp;
	}

	public Double getBinaryCutoffValue() {
		return binaryCutoffValue;
	}

	public void setBinaryCutoffValue(Double binaryCutoffValue) {
		this.binaryCutoffValue = binaryCutoffValue;
	}

	public ResourceRef getSourceDataResource() {
		return sourceDataResource;
	}

	public void setSourceDataResource(ResourceRef sourceDataResource) {
		this.sourceDataResource = sourceDataResource;
	}

	public ResourceRef getFilteredDataResource() {
		return filteredDataResource;
	}

	public void setFilteredDataResource(ResourceRef filteredDataResource) {
		this.filteredDataResource = filteredDataResource;
	}

	public IMatrix getData() {
		return data;
	}

	public void setData(IMatrix data) {
		this.data = data;
	}

	public ResourceRef getCellResultsResource() {
		return cellResultsResource;
	}

	public void setCellResultsResource(ResourceRef cellResultsResource) {
		this.cellResultsResource = cellResultsResource;
	}

    @Deprecated
	public IMatrix getCellResults() {
		return cellResults;
	}

    public IMatrix getResult() {
        return cellResults;
    }

	public void setCellResults(IMatrix results) {
		this.cellResults = results;
	}
}
