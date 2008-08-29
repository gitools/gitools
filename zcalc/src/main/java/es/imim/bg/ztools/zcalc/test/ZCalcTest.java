package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;

public interface ZCalcTest {

	String getName();
	
	String[] getResultNames();

	void processPopulation(String name, DoubleMatrix1D population);
	
	ZCalcResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
