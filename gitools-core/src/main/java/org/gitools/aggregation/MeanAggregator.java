package org.gitools.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

/** Mean */
public class MeanAggregator implements IAggregator {
	
	@Override
	public double aggregate(DoubleMatrix1D data) {
		double sum = data.aggregate(Functions.plus, Functions.identity);
		return sum / data.size();
	}

	@Override
	public String toString() {
		return "Mean";
	}
}
