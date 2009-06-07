package org.gitools.stats.test.results;

import org.gitools.model.table.element.Property;

public class FisherResult extends CommonResult {

	public int a;
	public int b;
	public int c;
	public int d;
	
	public FisherResult() {
		super(0, 0.0, 0.0, 0.0);
		a = b = c = d = 0;
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
	}
	
	@Property(id = "a", name = "a", description = "Number of positive events that belongs to the module")
	public int getA() {
		return a;
	}
	
	public void setA(int a) {
		this.a = a;
	}
	
	@Property(id = "b", name = "b", description = "Number of no positive events that belongs to the module")
	public int getB() {
		return b;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	
	@Property(id = "c", name = "c", description = "Number of positive events that don't belong to the module")
	public int getC() {
		return c;
	}
	
	public void setC(int c) {
		this.c = c;
	}
	
	@Property(id = "d", name = "d", description = "Number of no positive events that don't belong to the module")
	public int getD() {
		return d;
	}
	
	public void setD(int d) {
		this.d = d;
	}
}
