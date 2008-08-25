package es.imim.bg.ztools.zcalc.test.factory;

import es.imim.bg.ztools.zcalc.test.PoissonZCalcTest;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;

@Deprecated
public class PoissonZCalcTestFactory implements ZCalcTestFactory {

	@Override
	public ZCalcTest create() {
		return new PoissonZCalcTest();
	}

}
