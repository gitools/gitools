package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;

import es.imim.bg.ztools.zcalc.statcalc.CountOnesStatisticCalc;

@Deprecated
public class ZscoreBinomialZCalcTest extends ZscoreZCalcTest {

	private double p;
	
	public ZscoreBinomialZCalcTest() {
		super(new CountOnesStatisticCalc());
	}

	@Override
	public String getName() {
		return "binomial-normal";
	}
	
	@Override
	public void startCondition(String propName, DoubleMatrix1D propItems) {
		
		super.startCondition(propName, propItems);
		
		p = statCalc.calc(population) / population.size();
	}
	
	@Override
	protected void infereMeanAndStdev(
			DoubleMatrix1D population, DoubleMatrix1D groupItems, PopulationStatistics expected) {
	
		int n = groupItems.size();
		
		expected.mean = n * p;
		expected.stdev = Math.sqrt(n * p * (1.0 - p));
	}

}
