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

package org.gitools.analysis.correlation;

import java.util.Properties;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement
public class CorrelationAnalysis extends Analysis {

	protected String method;

	protected Properties methodProperties;

	protected Double replaceNanValue;

	protected boolean transposeData;

	protected int attributeIndex;

	/** Data */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix data;

	/** Results */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix results;

	public CorrelationAnalysis() {
		this.method = PearsonCorrelationMethod.ID;
		this.methodProperties = new Properties();
		this.replaceNanValue = null;
		this.transposeData = false;
		this.attributeIndex = 0;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Properties getMethodProperties() {
		return methodProperties;
	}

	public void setMethodProperties(Properties methodProperties) {
		this.methodProperties = methodProperties;
	}

	public boolean isReplaceNanValues() {
		return replaceNanValue != null && !Double.isNaN(replaceNanValue);
	}

	public Double getReplaceNanValue() {
		return replaceNanValue;
	}

	public void setReplaceNanValue(Double value) {
		this.replaceNanValue = value;
	}

	public boolean isTransposeData() {
		return transposeData;
	}

	public void setTransposeData(boolean transposeData) {
		this.transposeData = transposeData;
	}

	public int getAttributeIndex() {
		return attributeIndex;
	}

	public void setAttributeIndex(int attributeIndex) {
		this.attributeIndex = attributeIndex;
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
