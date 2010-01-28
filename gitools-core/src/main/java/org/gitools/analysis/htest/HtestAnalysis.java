/*
 *  Copyright 2010 chris.
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

package org.gitools.analysis.htest;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncozAnalysis;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.Analysis;
import org.gitools.model.ToolConfig;
import org.gitools.model.xml.adapter.CutoffCmpXmlAdapter;
import org.gitools.model.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement

@XmlSeeAlso({
	EnrichmentAnalysis.class,
	OncozAnalysis.class})

@XmlAccessorType(XmlAccessType.FIELD)
public class HtestAnalysis extends Analysis {

	/** Data binary cutoff enabled */
	protected boolean binaryCutoffEnabled;

	/** Data binary cutoff comparator */
	@XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
	protected CutoffCmp binaryCutoffCmp;

	/** Data binary cutoff value */
	protected double binaryCutoffValue;

	/** Test name */
	protected ToolConfig testConfig;

	/** Data */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix dataMatrix;

	/** Multiple test correction */
	protected String mtc;

	/** Results */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected ObjectMatrix resultsMatrix;

	public HtestAnalysis() {
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

	public void setBinaryCutoffCmp(CutoffCmp comparator) {
		this.binaryCutoffCmp = comparator;
	}

	public double getBinaryCutoffValue() {
		return binaryCutoffValue;
	}

	public void setBinaryCutoffValue(double value) {
		this.binaryCutoffValue = value;
	}

	public ToolConfig getTestConfig() {
		return testConfig;
	}

	public void setTestConfig(ToolConfig testConfig) {
		this.testConfig = testConfig;
	}

	public IMatrix getDataMatrix() {
		return dataMatrix;
	}

	public void setDataMatrix(IMatrix dataTable) {
		this.dataMatrix = dataTable;
	}

	/*public DoubleMatrix getDataTable() {
		return dataTable;
	}

	public void setDataTable(DoubleMatrix dataTable) {
		this.dataTable = dataTable;
	}*/

	public String getMtc() {
		return mtc;
	}

	public void setMtc(String mtc) {
		this.mtc = mtc;
	}

	public ObjectMatrix getResultsMatrix() {
		return resultsMatrix;
	}

	public void setResultsMatrix(ObjectMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
