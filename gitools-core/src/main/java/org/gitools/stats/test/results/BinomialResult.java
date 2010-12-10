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

public class BinomialResult extends CommonResult {

	public enum Distribution {
		BINOMIAL, NORMAL, POISSON 
	};
	
	public Distribution distribution;
	public int observed;
	public double expectedMean;
	public double expectedStdev;
	public double probability;
	
	public BinomialResult() {
		super(0, 0.0, 0.0, 0.0);
		observed = 0;
		expectedMean = expectedStdev = 0;
		probability = 0;
	}
	
	public BinomialResult(
			Distribution aprox, int n,
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			int observed, double expectedMean, double expectedStdev, double p) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.distribution = aprox;
		this.observed = observed;
		this.expectedMean = expectedMean;
		this.expectedStdev = expectedStdev;
		this.probability = p;
	}
	
	@AttributeDef(id = "observed", name = "Observed events", description = "Number of positive events observed")
	public int getObserved() {
		return observed;
	}
	
	public void setObserved(int observed) {
		this.observed = observed;
	}
	
	@AttributeDef(id = "expected-mean", name = "Expected mean", description = "Number of positive events expected by chance")
	public double getExpectedMean() {
		return expectedMean;
	}
	
	public void setExpectedMean(double expectedMean) {
		this.expectedMean = expectedMean;
	}
	
	@AttributeDef(id = "expected-stdev", name = "Expected stddev", description = "Standard deviation of the number of positive events expected by chance")
	public double getExpectedStdev() {
		return expectedStdev;
	}

	public void setExpectedStdev(double expectedStdev) {
		this.expectedStdev = expectedStdev;
	}
	
	@AttributeDef(id = "distribution", name = "Distribution", description = "Wich distribution has been used to do calculations (Binomial exact, Normal or Poisson)")
	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}
	
	@AttributeDef(id = "probability", name = "Probability", description = "Population probability of a positive event")
	public double getProbability() {
		return probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
