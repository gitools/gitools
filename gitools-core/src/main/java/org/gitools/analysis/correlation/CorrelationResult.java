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


public class CorrelationResult {

	protected int n;
	protected double score;
	/*protected double pvalue;
	protected double standardError;*/

	public CorrelationResult() {
	}

	public CorrelationResult(int n, double score/*, double pvalue, double standardError*/) {
		this.score = score;
		/*this.pvalue = pvalue;
		this.standardError = standardError;*/
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	/*public double getPvalue() {
		return pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}

	public double getStandardError() {
		return standardError;
	}

	public void setStandardError(double standardError) {
		this.standardError = standardError;
	}*/
}
