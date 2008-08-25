package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;

public interface ZCalcTest {

	String getName();
	
	String[] getResultNames();

	void startCondition(String propName, DoubleMatrix1D propItems);
	
	ZCalcResult processGroup(
			String propName, DoubleMatrix1D propItems, 
			String groupName, int[] groupItemIndices);
}
