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

import org.gitools.matrix.model.element.AttributeDef;


public class CombinationResult {

	protected int n;

	protected double zscore;

	protected double pvalue;

	@AttributeDef(id = "N", name = "N", description = "Number of pvalues combined")
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	@AttributeDef(id = "z-score", name = "Z-Score", description = "Z-Score of the combination")
	public double getZscore() {
		return zscore;
	}

	public void setZscore(double zscore) {
		this.zscore = zscore;
	}

	@AttributeDef(id = "p-value", name = "P-Value", description = "Combined P-Value")
	public double getPvalue() {
		return pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}
}
