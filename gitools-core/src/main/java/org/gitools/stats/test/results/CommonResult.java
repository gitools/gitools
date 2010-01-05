package org.gitools.stats.test.results;

import java.io.Serializable;

import org.gitools.matrix.model.element.Property;

public class CommonResult implements Serializable{

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
	
	public void setN(int n) {
		N = n;
	}
	
	@Property(id = "left-p-value", name = "Left P-Value", description = "P-Value for alternative hipothesis lower than")
	public double getLeftPvalue() {
		return leftPvalue;
	}
	
	public void setLeftPvalue(double leftPvalue) {
		this.leftPvalue = leftPvalue;
	}
	
	@Property(id = "right-p-value", name = "Right P-Value", description = "P-Value for alternative hipothesis greater than")
	public double getRightPvalue() {
		return rightPvalue;
	}
	
	public void setRightPvalue(double rightPvalue) {
		this.rightPvalue = rightPvalue;
	}
	
	@Property(id = "two-tail-p-value", name = "Two tail P-Value", description = "P-Value for alternative hipothesis different than")
	public double getTwoTailPvalue() {
		return twoTailPvalue;
	}
	
	public void setTwoTailPvalue(double twoTailPvalue) {
		this.twoTailPvalue = twoTailPvalue;
	}
	
	@Property(id = "corrected-left-p-value", name = "Corrected left P-Value", description = "Corrected P-Value for alternative hipothesis lower than")
	public double getCorrLeftPvalue() {
		return corrLeftPvalue;
	}
	
	public void setCorrLeftPvalue(double corrLeftPvalue) {
		this.corrLeftPvalue = corrLeftPvalue;
	}
	
	@Property(id = "corrected-right-p-value", name = "Corrected right P-Value", description = "Corrected P-Value for alternative hipothesis greater than")
	public double getCorrRightPvalue() {
		return corrRightPvalue;
	}

	public void setCorrRightPvalue(double corrRightPvalue) {
		this.corrRightPvalue = corrRightPvalue;
	}
	
	@Property(id = "corrected-two-tail-p-value", name = "Corrected two tail P-Value", description = "Corrected P-Value for alternative hipothesis different than")
	public double getCorrTwoTailPvalue() {
		return corrTwoTailPvalue;
	}
	
	public void setCorrTwoTailPvalue(double corrTwoTailPvalue) {
		this.corrTwoTailPvalue = corrTwoTailPvalue;
	}
}
