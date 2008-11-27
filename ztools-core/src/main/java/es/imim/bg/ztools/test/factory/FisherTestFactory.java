package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.model.TestConfig;
import es.imim.bg.ztools.test.FisherTest;
import es.imim.bg.ztools.test.Test;

public final class FisherTestFactory extends TestFactory {

	public FisherTestFactory(TestConfig config) {
		super(config);
	}

	@Override
	public Test create() {
		return new FisherTest();
	}

}
