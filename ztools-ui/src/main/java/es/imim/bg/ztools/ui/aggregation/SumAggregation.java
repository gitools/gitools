package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

public class SumAggregation implements IAggregation {

	
	@Override
	public double aggregate(DoubleMatrix1D row) {
		//sum
		double a = row.aggregate(Functions.plus, Functions.identity);
		return a;
	}

}
