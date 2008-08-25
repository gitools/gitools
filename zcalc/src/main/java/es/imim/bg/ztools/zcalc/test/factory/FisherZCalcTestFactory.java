package es.imim.bg.ztools.zcalc.test.factory;

import es.imim.bg.ztools.zcalc.test.FisherZCalcTest;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;

public final class FisherZCalcTestFactory implements ZCalcTestFactory {

	@Override
	public ZCalcTest create() {
		return new FisherZCalcTest();
	}

}
