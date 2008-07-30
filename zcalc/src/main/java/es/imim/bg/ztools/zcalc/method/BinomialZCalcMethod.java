package es.imim.bg.ztools.zcalc.method;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;
import es.imim.bg.ztools.zcalc.results.BinomialResult;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;
import es.imim.bg.ztools.zcalc.statcalc.CountOnesStatisticCalc;
import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;

public class BinomialZCalcMethod extends AbstractZCalcMethod {

	public enum AproximationMode {
		onlyExact, onlyNormal, onlyPoisson, automatic
	};
	
	protected StatisticCalc statCalc;
	
	protected AproximationMode aproxMode;
	
	protected DoubleMatrix1D population;
	
	private double p;
	
	public BinomialZCalcMethod(AproximationMode aproxMode) {
		this.statCalc = new CountOnesStatisticCalc();
		this.aproxMode = aproxMode;
	}
	
	@Override
	public String getName() {
		return "binomial";
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
		
		// Create a view with group values (excluding NaN's)
		
		final DoubleMatrix1D groupItems =
			propItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);
		
		// Calculate observed statistic
		
		int observed = (int) statCalc.calc(groupItems);
		
		// Calculate expected mean and standard deviation
	
		int n = groupItems.size();
		
		double expectedMean = n * p;
		double expectedVar = n * p * (1.0 - p);
		double expectedStdev = Math.sqrt(expectedVar);
		
		switch (aproxMode) {
		case onlyExact: 
			return resultWithExact(observed, n, expectedMean, expectedStdev);
		case onlyNormal: 
			return resultWithNormal(observed, n, expectedMean, expectedStdev);
		case onlyPoisson: 
			return resultWithPoisson(observed, n, expectedMean, expectedStdev);
		case automatic:
			if (n <= 1000)
				return resultWithExact(observed, n, expectedMean, expectedStdev);
			else if ((n * p >= 5) && (n * (1 - p) >= 5))
				return resultWithNormal(observed, n, expectedMean, expectedStdev);
			else if (n >= 150 && expectedVar >= 0.9 * expectedMean)
				return resultWithPoisson(observed, n, expectedMean, expectedStdev);
			else 
				return null;

		default:
			return null;
		}
	}
	
	public ZCalcResult resultWithExact(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		return null;
	}
	
	public ZCalcResult resultWithNormal(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		double zscore;
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Calculate zscore and pvalues
		
		zscore = (observed - expectedMean) / expectedStdev;
		
		leftPvalue = Probability.normal(zscore);
		rightPvalue = 1.0 - leftPvalue; // Right tail
		twoTailPvalue = zscore <= 0 ? leftPvalue : rightPvalue;
		
		return new BinomialResult(
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}
	
	public ZCalcResult resultWithPoisson(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Calculate pvalues
		
		try {
			leftPvalue = Probability.poisson(observed, expectedMean);
			rightPvalue = 1.0 - leftPvalue;
			twoTailPvalue = observed <= expectedMean  ? leftPvalue : rightPvalue; 
		}
		catch (ArithmeticException e) {
			leftPvalue = rightPvalue = twoTailPvalue = Double.NaN;
		}
		
		return new BinomialResult(
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}

}
