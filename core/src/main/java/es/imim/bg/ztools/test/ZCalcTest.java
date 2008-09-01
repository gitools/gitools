package es.imim.bg.ztools.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.test.results.ZCalcResult;

public interface ZCalcTest {

	String getName();
	
	String[] getResultNames();

	void processPopulation(String name, DoubleMatrix1D population);
	
	ZCalcResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
