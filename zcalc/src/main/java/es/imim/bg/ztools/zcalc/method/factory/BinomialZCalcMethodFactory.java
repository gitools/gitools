package es.imim.bg.ztools.zcalc.method.factory;

import es.imim.bg.ztools.zcalc.method.BinomialZCalcMethod;
import es.imim.bg.ztools.zcalc.method.ZCalcMethod;
import es.imim.bg.ztools.zcalc.method.BinomialZCalcMethod.AproximationMode;

public class BinomialZCalcMethodFactory implements ZCalcMethodFactory {

	private AproximationMode aproxMode;
	
	public BinomialZCalcMethodFactory(AproximationMode aproxMode) {
		this.aproxMode = aproxMode;
	}
	
	@Override
	public ZCalcMethod create() {
		return new BinomialZCalcMethod(aproxMode);
	}

}
