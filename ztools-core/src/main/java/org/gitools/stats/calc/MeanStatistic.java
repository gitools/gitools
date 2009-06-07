package org.gitools.stats.calc;

import cern.colt.matrix.DoubleMatrix1D;

public class MeanStatistic implements Statistic {

	@Override
	public String getName() {
		return "mean";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		return values.zSum() / values.size();
	}
}
