package es.imim.bg.ztools.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

/** Multiplication */
public class MultAggregator implements IAggregator {
	
	@Override
	public double aggregate(DoubleMatrix1D row) {
		return row.aggregate(Functions.mult, Functions.identity);
	}

	@Override
	public String toString() {
		return "Multiplication";
	}
}
