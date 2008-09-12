package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.stats.calc.StatisticCalc;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.ZscoreWithSamplingTest;

public final class ZscoreWithSamplingTestFactory implements TestFactory {

	private int numSamples;
	private StatisticCalc statCalc;
	
	public ZscoreWithSamplingTestFactory(
			int numSamples,
			StatisticCalc statCalc) {
	
		this.numSamples = numSamples;
		this.statCalc = statCalc;
	}

	@Override
	public Test create() {
		return new ZscoreWithSamplingTest(numSamples, statCalc);
	}

}
