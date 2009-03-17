package es.imim.bg.ztools.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

/** Sum */
public class SumAggregator implements IAggregator {

	@Override
	public double aggregate(DoubleMatrix1D row) {
		return row.aggregate(Functions.plus, Functions.identity);
	}

	@Override
	public String toString() {
		return "Sum";
	}
}
