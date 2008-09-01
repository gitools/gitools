package es.imim.bg.ztools.test.results;

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
		array[i + 3] = "z-score";
		
		return i + 4;
	}
	
	@Override
	protected int fillParamValues(Object[] array) {
		int i = super.fillParamValues(array);
		
		array[i + 0] = observed;
		array[i + 1] = expectedMean;
		array[i + 2] = expectedStdev;
		array[i + 3] = zscore;
		
		return i + 4;
	}
}
