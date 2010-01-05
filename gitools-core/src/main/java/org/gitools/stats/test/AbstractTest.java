package org.gitools.stats.test;

import cern.colt.function.DoubleProcedure;

public abstract class AbstractTest implements Test {

	protected static final DoubleProcedure notNaNProc = 
		new DoubleProcedure() {
			public boolean apply(double element) {
				return !Double.isNaN(element);
			}
		};
}
