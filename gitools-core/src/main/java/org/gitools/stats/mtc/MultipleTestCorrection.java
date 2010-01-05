package org.gitools.stats.mtc;

import cern.colt.matrix.DoubleMatrix1D;

public interface MultipleTestCorrection {

	String getName();
	
	void correct(DoubleMatrix1D values);
}
