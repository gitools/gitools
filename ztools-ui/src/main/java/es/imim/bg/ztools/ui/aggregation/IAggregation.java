package es.imim.bg.ztools.ui.aggregation;

import cern.colt.matrix.DoubleMatrix1D;


public interface IAggregation {
	
	double aggregate(double[] row); 
	
}
