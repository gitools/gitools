package es.imim.bg.ztools.test.results;

import es.imim.bg.ztools.model.elements.Property;

public class CommonResult {

	public int N;
	public double leftPvalue;
	public double rightPvalue;
	public double twoTailPvalue;
	public double corrLeftPvalue;
	public double corrRightPvalue;
	public double corrTwoTailPvalue;
	
	public CommonResult(
			int N,
			double leftPvalue, double rightPvalue,
			double twoTailPvalue) {
	
		this.leftPvalue = leftPvalue;
		this.N = N;
		this.rightPvalue = rightPvalue;
		this.twoTailPvalue = twoTailPvalue;
	}
	
	@Property(id = "N", name = "N", description = "Number of elements")
	public int getN() {
		return N;
	}
	
	@Property(id = "left-p-value", name = "Left P-Value", description = "P-Value for alternative hipothesis lower than")
	public double getLeftPvalue() {
		return leftPvalue;
	}
	
	@Property(id = "right-p-value", name = "Right P-Value", description = "P-Value for alternative hipothesis greater than")
	public double getRightPvalue() {
		return rightPvalue;
	}
	
	@Property(id = "two-tail-p-value", name = "Two tail P-Value", description = "P-Value for alternative hipothesis different than")
	public double getTwoTailPvalue() {
		return twoTailPvalue;
	}
	
	@Property(id = "corrected-left-p-value", name = "Corrected left P-Value", description = "Corrected P-Value for alternative hipothesis lower than")
	public double getCorrLeftPvalue() {
		return corrLeftPvalue;
	}
	
	@Property(id = "corrected-right-p-value", name = "Corrected right P-Value", description = "Corrected P-Value for alternative hipothesis greater than")
	public double getCorrRightPvalue() {
		return corrRightPvalue;
	}

	@Property(id = "corrected-two-tail-p-value", name = "Corrected two tail P-Value", description = "Corrected P-Value for alternative hipothesis different than")
	public double getCorrTwoTailPvalue() {
		return corrTwoTailPvalue;
	}
}
