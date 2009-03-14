package es.imim.bg.ztools.test.results;

import es.imim.bg.ztools.table.element.Property;

public class CombinationResult {

	private int N;
	private double zscore;
	private double pvalue;
	
	public CombinationResult() {
	}
	
	@Property(id = "N", name = "N", description = "Number of elements")
	public int getN() {
		return N;
	}
	
	public void setN(int n) {
		N = n;
	}
	
	@Property(id = "z-score", name = "Z Score", description = "Combined Z Score")
	public double getZscore() {
		return zscore;
	}
	
	public void setZscore(double zscore) {
		this.zscore = zscore;
	}
	
	@Property(id = "p-value", name = "P-Value", description = "Combined P-Value")
	public double getPvalue() {
		return pvalue;
	}
	
	public void setPvalue(double value) {
		pvalue = value;
	}
}
