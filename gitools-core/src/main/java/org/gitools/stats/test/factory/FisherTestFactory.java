package org.gitools.stats.test.factory;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.FisherTest;
import org.gitools.stats.test.Test;

public final class FisherTestFactory extends TestFactory {

	public FisherTestFactory(ToolConfig config) {
		super(config);
	}

	@Override
	public Test create() {
		return new FisherTest();
	}

}
