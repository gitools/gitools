package es.imim.bg.ztools.test.results;

public class CommonResult extends AbstractResult {

	public int numItems;
	public double leftPvalue;
	public double rightPvalue;
	public double twoTailPvalue;
	
	
	public CommonResult(
			int numItems,
			double leftPvalue, double rightPvalue,
			double twoTailPvalue) {
	
		this.leftPvalue = leftPvalue;
		this.numItems = numItems;
		this.rightPvalue = rightPvalue;
		this.twoTailPvalue = twoTailPvalue;
	}

	protected int getNumParams() {
		return 4;
	}
	
	protected int fillParamNames(String[] array) {
		array[0] = "N";
		array[1] = "left-p-value";
		array[2] = "right-p-value";
		array[3] = "two-tail-p-value";
		return 4;
	}
	
	protected int fillParamValues(Object[] array) {
		array[0] = numItems;
		array[1] = leftPvalue;
		array[2] = rightPvalue;
		array[3] = twoTailPvalue;
		return 4;
	}

	public int getN() {
		return numItems;
	}
}
