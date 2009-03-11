package es.imim.bg.ztools.test.results;

import es.imim.bg.ztools.table.element.Property;

public class CombinationResult {

	private int N;
	private double pValue;
	
	public CombinationResult() {
	}
	
	@Property(id = "N", name = "N", description = "Number of elements")
	public int getN() {
		return N;
	}
	
	public void setN(int n) {
		N = n;
	}
	
	@Property(id = "p-value", name = "P-Value", description = "Combined P-Value")
	public double getPValue() {
		return pValue;
	}
	
	public void setPValue(double value) {
		pValue = value;
	}
}
