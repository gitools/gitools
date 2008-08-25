package es.imim.bg.ztools.stats.multitestcorrection;

import cern.colt.matrix.DoubleMatrix1D;

public interface MultipleTestCorrection {

	void correct(DoubleMatrix1D pvalues, DoubleMatrix1D correctedPvalues);
}
