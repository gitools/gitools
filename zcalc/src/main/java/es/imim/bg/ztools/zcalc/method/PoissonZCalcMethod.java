package es.imim.bg.ztools.zcalc.method;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;
import es.imim.bg.ztools.zcalc.results.BinomialResult;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;
import es.imim.bg.ztools.zcalc.statcalc.CountOnesStatisticCalc;
import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;

public class PoissonZCalcMethod extends AbstractZCalcMethod {
	
	protected StatisticCalc statCalc;
	
	protected DoubleMatrix1D population;
	
	private double p;
	
	@Override
	public String getName() {
		return "poisson";
	}

	public PoissonZCalcMethod() {
		this.statCalc = new CountOnesStatisticCalc();
	}
	
	@Override
	public String[] getResultNames() {
		return new BinomialResult().getNames();
	}

	@Override
	public void startCondition(String propName, DoubleMatrix1D propItems) {
		population = propItems.viewSelection(notNaNProc);
		p = statCalc.calc(population) / population.size();
	}

	@Override
	public ZCalcResult processGroup(
			String propName, DoubleMatrix1D propItems,
			String groupName, int[] groupItemIndices) {
		
		int observed;
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Create a view with group values (excluding NaN's)
		
		final DoubleMatrix1D groupItems =
			propItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);
		
		// Calculate observed statistic
		
		observed = (int) statCalc.calc(groupItems);
		
		// Calculate expected mean and standard deviation from sampling
	
		int n = groupItems.size();
		
		double expectedMean = n * p;
		double expectedStdev = Math.sqrt(n * p * (1.0 - p));
		
		// Calculate zscore and pvalue
		try {
			leftPvalue = Probability.poisson(observed, expectedMean);
			rightPvalue = 1.0 - leftPvalue;
			twoTailPvalue = (observed <= expectedMean  ? leftPvalue : rightPvalue) * 2; //FIXME: Review 
		}
		catch (ArithmeticException e) {
			leftPvalue = rightPvalue = twoTailPvalue = Double.NaN;
		}
		
		return new BinomialResult(
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}
}
