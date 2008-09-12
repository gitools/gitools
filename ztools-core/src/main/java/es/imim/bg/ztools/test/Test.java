package es.imim.bg.ztools.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.test.results.Result;

public interface Test {

	String getName();
	
	String[] getResultNames();

	void processPopulation(String name, DoubleMatrix1D population);
	
	Result processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
