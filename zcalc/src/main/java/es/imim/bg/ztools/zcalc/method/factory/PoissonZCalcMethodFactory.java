package es.imim.bg.ztools.zcalc.method.factory;

import es.imim.bg.ztools.zcalc.method.PoissonZCalcMethod;
import es.imim.bg.ztools.zcalc.method.ZCalcMethod;

public class PoissonZCalcMethodFactory implements ZCalcMethodFactory {

	@Override
	public ZCalcMethod create() {
		return new PoissonZCalcMethod();
	}

}
