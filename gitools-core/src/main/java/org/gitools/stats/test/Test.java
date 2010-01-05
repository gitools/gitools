package org.gitools.stats.test;

import org.gitools.stats.test.results.CommonResult;

import cern.colt.matrix.DoubleMatrix1D;

public interface Test {

	String getName();
	
	//String[] getResultNames();
	
	Class<? extends  CommonResult> getResultClass();

	void processPopulation(String name, DoubleMatrix1D population);
	
	CommonResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
