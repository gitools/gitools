package es.imim.bg.ztools.zcalc.test;

import cern.colt.function.DoubleProcedure;

public abstract class AbstractZCalcTest implements ZCalcTest {

	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};
}
