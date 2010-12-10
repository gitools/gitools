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

package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.AttributeDef;

public class ZScoreResult extends CommonResult {

	public double observed;
	public double expectedMean;
	public double expectedStdev;
	public double zscore;
	
	public ZScoreResult() {
		super(0, 0.0, 0.0, 0.0);
		observed = expectedMean = expectedStdev = 0;
		zscore = 0;
	}
	
	public ZScoreResult(
			int n,
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			double observed, 
			double expectedMean, double expectedStdev, double zscore) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.observed = observed;
		this.expectedMean = expectedMean;
		this.expectedStdev = expectedStdev;
		this.zscore = zscore;
	}

	@AttributeDef(id = "observed", name = "Observed value", description = "Value observed")
	public double getObserved() {
		return observed;
	}
	
	public void setObserved(double observed) {
		this.observed = observed;
	}
	
	@AttributeDef(id = "expected-mean", name = "Expected mean", description = "Value mean expected by chance")
	public double getExpectedMean() {
		return expectedMean;
	}
	
	public void setExpectedMean(double expectedMean) {
		this.expectedMean = expectedMean;
	}
	
	@AttributeDef(id = "expected-stdev", name = "Expected stddev", description = "Value standard deviation expected by chance")
	public double getExpectedStdev() {
		return expectedStdev;
	}
	
	public void setExpectedStdev(double expectedStdev) {
		this.expectedStdev = expectedStdev;
	}
	
	@AttributeDef(id = "z-score", name = "Z Score", description = "Normal distribution Z Score")
	public double getZscore() {
		return zscore;
	}
	
	public void setZscore(double zscore) {
		this.zscore = zscore;
	}
}
