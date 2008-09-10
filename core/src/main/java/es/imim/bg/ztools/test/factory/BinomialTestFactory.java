package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.test.BinomialTest;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.BinomialTest.AproximationMode;

public class BinomialTestFactory implements TestFactory {

	private AproximationMode aproxMode;
	
	public BinomialTestFactory(AproximationMode aproxMode) {
		this.aproxMode = aproxMode;
	}
	
	@Override
	public Test create() {
		return new BinomialTest(aproxMode);
	}

}
