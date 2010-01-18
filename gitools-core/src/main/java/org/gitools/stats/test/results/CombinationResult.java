package org.gitools.stats.test.results;

import org.gitools.matrix.model.element.AttributeDef;

public class CombinationResult {

	private int N;
	private double zscore;
	private double pvalue;
	
	public CombinationResult() {
	}
	
	@AttributeDef(id = "N", name = "N", description = "Number of elements")
	public int getN() {
		return N;
	}
	
	public void setN(int n) {
		N = n;
	}
	
	@AttributeDef(id = "z-score", name = "Z Score", description = "Combined Z Score")
	public double getZscore() {
		return zscore;
	}
	
	public void setZscore(double zscore) {
		this.zscore = zscore;
	}
	
	@AttributeDef(id = "p-value", name = "P-Value", description = "Combined P-Value")
	public double getPvalue() {
		return pvalue;
	}
	
	public void setPvalue(double value) {
		pvalue = value;
	}
}
