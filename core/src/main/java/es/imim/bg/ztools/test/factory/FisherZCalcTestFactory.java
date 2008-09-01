package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.test.FisherZCalcTest;
import es.imim.bg.ztools.test.ZCalcTest;

public final class FisherZCalcTestFactory implements ZCalcTestFactory {

	@Override
	public ZCalcTest create() {
		return new FisherZCalcTest();
	}

}
