package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

public class LogSumAggregation implements IAggregation {

	
	@Override
	public double aggregate(DoubleMatrix1D row) {
		//sum the logs
		double a = row.aggregate(Functions.plus, Functions.log);
		return a;
	}

}
