package es.imim.bg.ztools.zcalc.method.factory;

import es.imim.bg.ztools.zcalc.method.ZCalcMethod;
import es.imim.bg.ztools.zcalc.method.ZscoreBinomialZCalcMethod;

public class ZscoreBinomialZCalcMethodFactory implements ZCalcMethodFactory {

	@Override
	public ZCalcMethod create() {
		return new ZscoreBinomialZCalcMethod();
	}

}
