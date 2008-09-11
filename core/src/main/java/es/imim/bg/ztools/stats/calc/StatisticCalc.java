package es.imim.bg.ztools.stats.calc;

import cern.colt.matrix.DoubleMatrix1D;

public interface StatisticCalc {

	String getName();
	
	double calc(DoubleMatrix1D values);
}
