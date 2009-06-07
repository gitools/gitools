package org.gitools.stats.mtc;

import cern.colt.matrix.DoubleMatrix1D;

public class BenjaminiHochbergFdr implements MultipleTestCorrection {

	@Override
	public String getName() {
		return "Benjamini Hochberg FDR";
	}
	
	@Override
	public void correct(final DoubleMatrix1D values) {
		
		DoubleMatrix1D sortedValues = values.viewSorted();
		
		int m = sortedValues.size();
		for (int idx = 0; idx < m; idx++) {
			int rank = idx + 1;
			double p = sortedValues.get(idx);
			sortedValues.set(idx, Math.min(1.0, p * m / rank));
		}
	}
}