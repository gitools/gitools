package es.imim.bg.ztools.zcalc.test.factory;

import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;
import es.imim.bg.ztools.zcalc.test.ZCalcTest;
import es.imim.bg.ztools.zcalc.test.ZscoreWithSamplingZCalcTest;

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
