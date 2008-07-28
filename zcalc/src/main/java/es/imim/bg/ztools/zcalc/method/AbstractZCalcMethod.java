package es.imim.bg.ztools.zcalc.method;

import cern.colt.function.DoubleProcedure;

public abstract class AbstractZCalcMethod implements ZCalcMethod {

	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};
}
