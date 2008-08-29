package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;

public interface ZCalcTest {

	String getName();
	
	String[] getResultNames();

	void startCondition(String condName, DoubleMatrix1D condItems);
	
	ZCalcResult processGroup(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices);
}
