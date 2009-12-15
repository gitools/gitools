package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.Property;

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
	
	@Property(id = "observed", name = "Observed events", description = "Number of positive events observed")
	public int getObserved() {
		return observed;
	}
	
	public void setObserved(int observed) {
		this.observed = observed;
	}
	
	@Property(id = "expected-mean", name = "Expected mean", description = "Number of positive events expected by chance")
	public double getExpectedMean() {
		return expectedMean;
	}
	
	public void setExpectedMean(double expectedMean) {
		this.expectedMean = expectedMean;
	}
	
	@Property(id = "expected-stdev", name = "Expected stddev", description = "Standard deviation of the number of positive events expected by chance")
	public double getExpectedStdev() {
		return expectedStdev;
	}

	public void setExpectedStdev(double expectedStdev) {
		this.expectedStdev = expectedStdev;
	}
	
	@Property(id = "distribution", name = "Distribution", description = "Wich distribution has been used to do calculations (Binomial exact, Normal or Poisson)")
	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}
	
	@Property(id = "probability", name = "Probability", description = "Population probability of a positive event")
	public double getProbability() {
		return probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
