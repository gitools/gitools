package es.imim.bg.ztools.zcalc.test.factory;

import es.imim.bg.ztools.zcalc.test.ZCalcTest;
import es.imim.bg.ztools.zcalc.test.ZscoreBinomialZCalcTest;

@Deprecated
public class ZscoreBinomialZCalcTestFactory implements ZCalcTestFactory {

	@Override
	public ZCalcTest create() {
		return new ZscoreBinomialZCalcTest();
	}

}
