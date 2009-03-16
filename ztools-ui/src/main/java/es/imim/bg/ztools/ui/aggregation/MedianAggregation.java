package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;

public class MedianAggregation implements IAggregation {

	
	@Override
	public double aggregate(DoubleMatrix1D row) {
		//sum
		double a = row.aggregate(Functions.plus, Functions.identity);
		//divide by elements
		int els = row.size();
		return a/els;
	}

}
