package es.imim.bg.ztools.test.results;

public class FisherResult extends CommonResult {

	public int a;
	public int b;
	public int c;
	public int d;
	
	//public double zscore; //FIXME RE compatibility
	
	public FisherResult() {
		super(0, 0.0, 0.0, 0.0);
		a = b = c = d = 0;
		leftPvalue = rightPvalue = twoTailPvalue = 0;
		//zscore = 0;
	}
	
	public FisherResult(
			int n, 
			double leftPvalue, double rightPvalue, double twoTailPvalue, 
			int a, int b, int c, int d) {
		
		super(n, leftPvalue, rightPvalue, twoTailPvalue);
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
		//this.zscore = Util.pvalue2zscore(rightPvalue); //FIXME
	}
	
	@Override
	protected int getNumParams() {
		return super.getNumParams() + 4;
	}
	
	@Override
	protected int fillParamNames(String[] array) {
		int i = super.fillParamNames(array);
		
		array[i + 0] = "a";
		array[i + 1] = "b";
		array[i + 2] = "c";
		array[i + 3] = "d";
		//array[i + 7] = "z-score";
		
		return i + 4;
	}
	
	@Override
	protected int fillParamValues(double[] array) {
		int i = super.fillParamValues(array);
		
		array[i + 0] = a;
		array[i + 1] = b;
		array[i + 2] = c;
		array[i + 3] = d;
		//array[i + 7] = zscore;
		
		return i + 4;
	}

}
