package es.imim.bg.ztools.zcalc.method.factory;

import es.imim.bg.ztools.zcalc.method.FisherZCalcMethod;
import es.imim.bg.ztools.zcalc.method.ZCalcMethod;

public final class FisherZCalcMethodFactory implements ZCalcMethodFactory {

	@Override
	public ZCalcMethod create() {
		return new FisherZCalcMethod();
	}

}
