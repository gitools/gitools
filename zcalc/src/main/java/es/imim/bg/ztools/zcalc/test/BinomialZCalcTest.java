package es.imim.bg.ztools.zcalc.test;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;
import es.imim.bg.ztools.zcalc.results.BinomialResult;
import es.imim.bg.ztools.zcalc.results.ZCalcResult;
import es.imim.bg.ztools.zcalc.statcalc.CountOnesStatisticCalc;
import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;

public class BinomialZCalcTest extends AbstractZCalcTest {

	private static final int exactSizeLimit = 100000;
	
	public enum AproximationMode {
		onlyExact, onlyNormal, onlyPoisson, automatic
	};
	
	private abstract class BinomialAproximation {
		public abstract ZCalcResult getResult(
				int observed, int n, double expectedMean, double expectedStdev, double expectedVar);
	}
	
	protected StatisticCalc statCalc;
	
	protected AproximationMode aproxMode;
	
	protected DoubleMatrix1D population;
	
	private double p;

	private BinomialAproximation aprox;
	
	public BinomialZCalcTest(AproximationMode aproxMode) {
		this.statCalc = new CountOnesStatisticCalc();
		this.aproxMode = aproxMode;
		
		switch (aproxMode) {
		case onlyExact: 
			this.aprox = new BinomialAproximation() {
				@Override
				public ZCalcResult getResult(int observed, int n, double expectedMean, double expectedStdev, double expectedVar) {
					return resultWithExact(observed, n, expectedMean, expectedStdev);
				}
			};
			break;
		case onlyNormal: 
			this.aprox = new BinomialAproximation() {
				@Override
				public ZCalcResult getResult(int observed, int n, double expectedMean, double expectedStdev, double expectedVar) {
					return resultWithNormal(observed, n, expectedMean, expectedStdev);
				}
			};
			break;
		case onlyPoisson: 
			this.aprox = new BinomialAproximation() {
				@Override
				public ZCalcResult getResult(int observed, int n, double expectedMean, double expectedStdev, double expectedVar) {
					return resultWithPoisson(observed, n, expectedMean, expectedStdev);
				}
			};
			break;
		case automatic:
			this.aprox = new BinomialAproximation() {
				@Override
				public ZCalcResult getResult(int observed, int n, double expectedMean, double expectedStdev, double expectedVar) {
					if (n <= exactSizeLimit)
						return resultWithExact(observed, n, expectedMean, expectedStdev);
					else if (n >= 150 && expectedVar >= 0.9 * expectedMean)
						return resultWithPoisson(observed, n, expectedMean, expectedStdev);
					else if ((n * p >= 5) && (n * (1 - p) >= 5))
						return resultWithNormal(observed, n, expectedMean, expectedStdev);
					else 
						return resultWithExact(observed, n, expectedMean, expectedStdev);
				}
			};
			break;
		}
	}
	
	@Override
	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append("binomial");
		switch (aproxMode) {
		case automatic: break;
		case onlyExact: sb.append("-exact"); break;
		case onlyNormal: sb.append("-normal"); break;
		case onlyPoisson: sb.append("-poisson"); break;
		}
		return sb.toString();
	}

	@Override
	public String[] getResultNames() {
		return new BinomialResult().getNames();
	}

	@Override
	public void startCondition(String condName, DoubleMatrix1D condItems) {
		population = condItems.viewSelection(notNaNProc);
		p = statCalc.calc(population) / population.size();
	}
	
	@Override
	public ZCalcResult processGroup(
			String condName, DoubleMatrix1D condItems,
			String groupName, int[] groupItemIndices) {
		
		// Create a view with group values (excluding NaN's)
		
		final DoubleMatrix1D groupItems =
			condItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);
		
		// Calculate observed statistic
		
		int observed = (int) statCalc.calc(groupItems);
		
		// Calculate expected mean and standard deviation
	
		int n = groupItems.size();
		
		double expectedMean = n * p;
		double expectedVar = n * p * (1.0 - p);
		double expectedStdev = Math.sqrt(expectedVar);
		
		return aprox.getResult(observed, n, expectedMean, expectedStdev, expectedVar);
	}
	
	public final ZCalcResult resultWithExact(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;
		
		leftPvalue = Probability.binomial(observed, n, p);
		rightPvalue = Probability.binomialComplemented(observed, n, p);
		twoTailPvalue = leftPvalue + rightPvalue;
		
		return new BinomialResult(BinomialResult.AproximationUsed.exact,
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}
	
	public final ZCalcResult resultWithNormal(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		double zscore;
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Calculate zscore and pvalues
		
		zscore = (observed - expectedMean) / expectedStdev;
		
		leftPvalue = Probability.normal(zscore);
		rightPvalue = 1.0 - leftPvalue;
		twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
		
		return new BinomialResult(BinomialResult.AproximationUsed.normal,
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}
	
	public final ZCalcResult resultWithPoisson(
			int observed, int n, double expectedMean, double expectedStdev) {
		
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Calculate pvalues
		
		try {
			leftPvalue = Probability.poisson(observed, expectedMean);
			rightPvalue = 1.0 - leftPvalue;
			twoTailPvalue = (observed <= expectedMean  ? leftPvalue : rightPvalue) * 2; //FIXME: Review 
		}
		catch (ArithmeticException e) {
			leftPvalue = rightPvalue = twoTailPvalue = Double.NaN;
		}
		
		return new BinomialResult(BinomialResult.AproximationUsed.poisson,
				n, leftPvalue, rightPvalue, twoTailPvalue, 
				observed, expectedMean, expectedStdev);
	}

}
