package es.imim.bg.ztools.zcalc.test.factory;

import es.imim.bg.ztools.zcalc.test.BinomialZCalcTest;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;
import es.imim.bg.ztools.zcalc.test.BinomialZCalcTest.AproximationMode;

public class BinomialZCalcTestFactory implements ZCalcTestFactory {

	private AproximationMode aproxMode;
	
	public BinomialZCalcTestFactory(AproximationMode aproxMode) {
		this.aproxMode = aproxMode;
	}
	
	@Override
	public ZCalcTest create() {
		return new BinomialZCalcTest(aproxMode);
	}

}
