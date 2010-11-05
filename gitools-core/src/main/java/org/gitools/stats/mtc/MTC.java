package org.gitools.stats.mtc;

import cern.colt.matrix.DoubleMatrix1D;

//TODO rename to MTC
public interface MTC {

	String getName();
	
	void correct(DoubleMatrix1D values);
}
