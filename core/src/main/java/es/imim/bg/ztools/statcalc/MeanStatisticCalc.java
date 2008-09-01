package es.imim.bg.ztools.statcalc;

import cern.colt.matrix.DoubleMatrix1D;

public class MeanStatisticCalc implements StatisticCalc {

	@Override
	public String getName() {
		return "mean";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		return values.zSum() / values.size();
	}
}
