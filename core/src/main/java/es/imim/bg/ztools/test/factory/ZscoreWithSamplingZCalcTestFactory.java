package es.imim.bg.ztools.test.factory;

import es.imim.bg.ztools.statcalc.StatisticCalc;
import es.imim.bg.ztools.test.ZCalcTest;
import es.imim.bg.ztools.test.ZscoreWithSamplingZCalcTest;

public final class ZscoreWithSamplingZCalcTestFactory implements ZCalcTestFactory {

	private int numSamples;
	private StatisticCalc statCalc;
	
	public ZscoreWithSamplingZCalcTestFactory(
			int numSamples,
			StatisticCalc statCalc) {
	
		this.numSamples = numSamples;
		this.statCalc = statCalc;
	}

	@Override
	public ZCalcTest create() {
		return new ZscoreWithSamplingZCalcTest(numSamples, statCalc);
	}

}
