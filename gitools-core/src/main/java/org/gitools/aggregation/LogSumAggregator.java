package org.gitools.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

/** Sum of logarithms */
public class LogSumAggregator implements IAggregator {

	@Override
	public double aggregate(DoubleMatrix1D data) {
		return data.aggregate(Functions.plus, Functions.log);
	}

	@Override
	public String toString() {
		return "Sum of logarithms";
	}
}
