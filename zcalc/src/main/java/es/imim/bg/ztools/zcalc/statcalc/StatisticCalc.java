package es.imim.bg.ztools.zcalc.statcalc;

import cern.colt.matrix.DoubleMatrix1D;

public interface StatisticCalc {

	String getName();
	
	double calc(DoubleMatrix1D values);
}
