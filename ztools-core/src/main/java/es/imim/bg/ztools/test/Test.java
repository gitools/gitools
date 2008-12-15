package es.imim.bg.ztools.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.test.results.CommonResult;

public interface Test {

	String getName();
	
	//String[] getResultNames();
	
	Class<? extends  CommonResult> getResultClass();

	void processPopulation(String name, DoubleMatrix1D population);
	
	CommonResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
