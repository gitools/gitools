package es.imim.bg.ztools.test.results;

public class CommonResult extends AbstractResult {

	public int numItems;
	public double leftPvalue;
	public double rightPvalue;
	public double twoTailPvalue;
	public double corrLeftPvalue;
	public double corrRightPvalue;
	public double corrTwoTailPvalue;
	
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
		return 7;
	}
	
	protected int fillParamNames(String[] array) {
		array[0] = "N";
		array[1] = "left-p-value";
		array[2] = "right-p-value";
		array[3] = "two-tail-p-value";
		array[4] = "corrected-left-p-value";
		array[5] = "corrected-right-p-value";
		array[6] = "corrected-two-tail-p-value";
		return 7;
	}
	
	protected int fillParamValues(double[] array) {
		array[0] = numItems;
		array[1] = leftPvalue;
		array[2] = rightPvalue;
		array[3] = twoTailPvalue;
		array[4] = corrLeftPvalue;
		array[5] = corrRightPvalue;
		array[6] = corrTwoTailPvalue;
		return 7;
	}

	public int getN() {
		return numItems;
	}
}
