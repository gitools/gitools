package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.test.FisherTest;
import es.imim.bg.ztools.test.Test;

public final class FisherTestFactory implements TestFactory {

	@Override
	public Test create() {
		return new FisherTest();
	}

}
