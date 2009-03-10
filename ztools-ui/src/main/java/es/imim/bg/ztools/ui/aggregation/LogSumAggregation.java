package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.jet.math.Functions;

public class LogSumAggregation implements IAggregation {

	
	@Override
	public double aggregate(double[] row) {
		// TODO Auto-generated method stub
		DoubleMatrix1D dm1d = new DenseDoubleMatrix1D(row);
		//multiplicate
		double a = dm1d.aggregate(Functions.plus, Functions.log);
		return a;
	}

}
