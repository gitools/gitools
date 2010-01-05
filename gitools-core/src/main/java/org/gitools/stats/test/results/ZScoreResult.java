package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.Property;

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

	@Property(id = "observed", name = "Observed value", description = "Value observed")
	public double getObserved() {
		return observed;
	}
	
	public void setObserved(double observed) {
		this.observed = observed;
	}
	
	@Property(id = "expected-mean", name = "Expected mean", description = "Value mean expected by chance")
	public double getExpectedMean() {
		return expectedMean;
	}
	
	public void setExpectedMean(double expectedMean) {
		this.expectedMean = expectedMean;
	}
	
	@Property(id = "expected-stdev", name = "Expected stddev", description = "Value standard deviation expected by chance")
	public double getExpectedStdev() {
		return expectedStdev;
	}
	
	public void setExpectedStdev(double expectedStdev) {
		this.expectedStdev = expectedStdev;
	}
	
	@Property(id = "z-score", name = "Z Score", description = "Normal distribution Z Score")
	public double getZscore() {
		return zscore;
	}
	
	public void setZscore(double zscore) {
		this.zscore = zscore;
	}
}