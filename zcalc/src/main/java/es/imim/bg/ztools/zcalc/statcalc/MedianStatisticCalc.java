package es.imim.bg.ztools.zcalc.statcalc;

import java.util.Arrays;

import cern.colt.matrix.DoubleMatrix1D;

public class MedianStatisticCalc implements StatisticCalc {

	@Override
	public String getName() {
		return "median";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		
		final int size = values.size();
		
		double[] tmp = new double[size];

		values.toArray(tmp);
		
		Arrays.sort(tmp);
		
		final int middle = size / 2;
		double median = tmp[middle];
		
		if (size % 2 == 0)
			median = (tmp[middle - 1] + median) / 2.0;
		
		return median;
	}

}
