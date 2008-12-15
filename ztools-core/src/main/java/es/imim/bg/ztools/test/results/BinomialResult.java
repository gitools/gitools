package es.imim.bg.ztools.test.results;

import es.imim.bg.ztools.model.elements.Property;

public class BinomialResult extends CommonResult {

	public enum AproximationUsed {
		exact, normal, poisson 
	};
	
	public AproximationUsed aprox;
	public int observed;
	public double expectedMean;
	public double expectedStdev;
	public double p;
	
	public BinomialResult() {
		super(0, 0.0, 0.0, 0.0);
		observed = 0;
		expectedMean = expectedStdev = 0;
		p = 0;
	}
	
	public BinomialResult(
			AproximationUsed aprox, int n,
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			int observed, double expectedMean, double expectedStdev, double p) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.aprox = aprox;
		this.observed = observed;
		this.expectedMean = expectedMean;
		this.expectedStdev = expectedStdev;
		this.p = p;
	}
	
	@Property(id = "observed", name = "Observed events", description = "Number of positive events observed")
	public int getObserved() {
		return observed;
	}
	
	@Property(id = "expected-mean", name = "Expected mean", description = "Number of positive events expected by chance")
	public double getExpectedMean() {
		return expectedMean;
	}
	
	@Property(id = "expected-stdev", name = "Expected stddev", description = "Standard deviation of the number of positive events expected by chance")
	public double getExpectedStdev() {
		return expectedStdev;
	}

	@Property(id = "aproximation", name = "Aproximation distribution", description = "Wich distribution has been used to aproximate Binomial distribution (Normal, Poisson or No aproximation)")
	public AproximationUsed getAproximation() {
		return aprox;
	}

	@Property(id = "probability", name = "Probability", description = "Population probability of a positive event")
	public double getP() {
		return p;
	}
}
