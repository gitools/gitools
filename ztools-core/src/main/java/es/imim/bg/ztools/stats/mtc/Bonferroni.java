package es.imim.bg.ztools.stats.mtc;

import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;

public class Bonferroni implements MultipleTestCorrection {

	@Override
	public String getName() {
		return "Bonferroni";
	}
	
	@Override
	public void correct(DoubleMatrix1D values) {
		final int n = values.size();
		
		values.assign(new DoubleFunction() {
			@Override
			public double apply(double v) {
				return Math.min(1.0, v * n);
			}
		});
	}

}
