package org.gitools.stats.calc;

import java.util.Arrays;

import cern.colt.matrix.DoubleMatrix1D;

public class MedianStatistic implements Statistic {

	@Override
	public String getName() {
		return "median";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		
		final int size = values.size();
		
		if (size == 0)
			return Double.NaN;
		else if (size == 1)
			return values.getQuick(0);
		
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
