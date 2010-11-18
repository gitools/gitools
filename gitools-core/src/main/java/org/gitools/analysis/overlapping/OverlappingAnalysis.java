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

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.xml.adapter.CutoffCmpXmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;


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

	/** Data */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix data;

	/** Results */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix results;

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

	public IMatrix getData() {
		return data;
	}

	public void setData(IMatrix data) {
		this.data = data;
	}

	public IMatrix getResults() {
		return results;
	}

	public void setResults(IMatrix results) {
		this.results = results;
	}
}
