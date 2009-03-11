package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.jet.math.Functions;

public class MultAggregation implements IAggregation {

	
	@Override
	public double aggregate(DoubleMatrix1D row) {
		//multiplicate
		double a = row.aggregate(Functions.mult, Functions.identity);
		return a;
	}

}
