package es.imim.bg.ztools.zcalc.method.factory;

import es.imim.bg.ztools.zcalc.method.ZCalcMethod;
import es.imim.bg.ztools.zcalc.method.ZscoreWithSamplingZCalcMethod;
import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;

public final class ZscoreWithSamplingZCalcMethodFactory implements ZCalcMethodFactory {

	private int numSamples;
	private StatisticCalc statCalc;
	
	public ZscoreWithSamplingZCalcMethodFactory(
			int numSamples,
			StatisticCalc statCalc) {
	
		this.numSamples = numSamples;
		this.statCalc = statCalc;
	}

	@Override
	public ZCalcMethod create() {
		return new ZscoreWithSamplingZCalcMethod(numSamples, statCalc);
	}

}
