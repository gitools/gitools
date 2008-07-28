package es.imim.bg.ztools.zcalc.results;

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
	protected int fillParamValues(Object[] array) {
		int i = super.fillParamValues(array);
		
		array[i + 0] = a;
		array[i + 1] = b;
		array[i + 2] = c;
		array[i + 3] = d;
		//array[i + 7] = zscore;
		
		return i + 4;
	}

	/*private static final int paramA = 0;
	private static final int paramB = 1;
	private static final int paramC = 2;
	private static final int paramD = 3;
	private static final int paramZScore = 4;
	private static final int paramLeftPvalue = 5;
	private static final int paramRightPvalue = 6;
	private static final int paramTwoTailPvalue = 7;*/
	
	
	/*public Object getParamValue(int index) {
	switch(index) {
	case paramA: return a;
	case paramB: return b;
	case paramC: return c;
	case paramD: return d;
	case paramZScore: return zscore;
	case paramLeftPvalue: return leftPvalue;
	case paramRightPvalue: return rightPvalue;
	case paramTwoTailPvalue: return twoTailPvalue;
	default: return null;
	}
}*/
}
