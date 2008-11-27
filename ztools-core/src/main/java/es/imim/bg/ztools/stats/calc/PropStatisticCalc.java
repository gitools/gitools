package es.imim.bg.ztools.stats.calc;

import cern.colt.matrix.DoubleMatrix1D;

public class PropStatisticCalc implements Statistic {

	@Override
	public String getName() {
		return "prop";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		
		int size = values.size();
		int count = 0;
		
		for (int i = 0; i < size; i++)
			count += values.getQuick(i) == 1.0 ? 1 : 0;
		
		return (double) count / (double) size;
	}

}
