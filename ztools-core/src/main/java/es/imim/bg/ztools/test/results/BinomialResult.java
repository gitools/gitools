package es.imim.bg.ztools.test.results;

public class BinomialResult extends CommonResult {

	public enum AproximationUsed {
		exact, normal, poisson 
	};
	
	public AproximationUsed aprox;
	public int observed;
	public double expectedMean;
	public double expectedStdev;
	
	public BinomialResult() {
		super(0, 0.0, 0.0, 0.0);
		observed = 0;
		expectedMean = expectedStdev = 0;
	}
	
	public BinomialResult(
			AproximationUsed aprox, int n,
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			int observed, double expectedMean, double expectedStdev) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.aprox = aprox;
		this.observed = observed;
		this.expectedMean = expectedMean;
		this.expectedStdev = expectedStdev;
		
	}

	@Override
	protected int getNumParams() {
		return super.getNumParams() + 4;
	}
	
	@Override
	protected int fillParamNames(String[] array) {
		int i = super.fillParamNames(array);
		
		array[i + 0] = "observed";
		array[i + 1] = "expected-mean";
		array[i + 2] = "expected-stdev";
		array[i + 3] = "aproximation";
		
		return i + 4;
	}
	
	@Override
	protected int fillParamValues(double[] array) {
		int i = super.fillParamValues(array);
		
		array[i + 0] = observed;
		array[i + 1] = expectedMean;
		array[i + 2] = expectedStdev;
		array[i + 3] = aprox.ordinal();
		
		return i + 4;
	}
}
