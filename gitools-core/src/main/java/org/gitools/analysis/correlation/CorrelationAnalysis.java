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

package org.gitools.analysis.correlation;

import java.util.Properties;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Analysis;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement
public class CorrelationAnalysis extends Analysis {

	protected String methodId;

	protected Properties methodProperties;

	protected double nanValue;
	
	/** Data */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix data;

	/** Results */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix results;

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public Properties getMethodProperties() {
		return methodProperties;
	}

	public void setMethodProperties(Properties methodProperties) {
		this.methodProperties = methodProperties;
	}

	public double getNanValue() {
		return nanValue;
	}

	public void setNanValue(double nanValue) {
		this.nanValue = nanValue;
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
