package org.gitools.stats.test;

import org.gitools.stats.calc.Statistic;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.ZScoreResult;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;

public abstract class ZscoreTest extends AbstractTest {

	//public static final int DEFAULT_NUM_SAMPLES = 10000;
	
	protected class PopulationStatistics {
		public double mean;
		public double stdev;
	}
	
	protected Statistic statCalc;
	
	protected DoubleMatrix1D population;
	
	public ZscoreTest(Statistic statCalc) {
		this.statCalc = statCalc;
	}
	
	@Override
	public String getName() {
		return "zscore-" + statCalc.getName();
	}

	/*@Override
	public String[] getResultNames() {
		return new ZScoreResult().getNames();
	}*/
	
	@Override
	public Class<? extends CommonResult> getResultClass() {
		return ZScoreResult.class;
	}
	
	@Override
	public void processPopulation(String name, DoubleMatrix1D population) {
		this.population = population;
	}
	
	@Override
	public CommonResult processTest(
			String condName, DoubleMatrix1D condItems, 
			String groupName, int[] groupItemIndices) {
		
		double observed;
		double zscore;
		double leftPvalue;
		double rightPvalue;
		double twoTailPvalue;

		// Create a view with group values (excluding NaN's)
		
		final DoubleMatrix1D groupItems =
			condItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);
		
		// Calculate observed statistic
		
		observed = statCalc.calc(groupItems);
		
		// Calculate expected mean and standard deviation from sampling
	
		int sampleSize = groupItems.size();
		
		PopulationStatistics expected = new PopulationStatistics(); 
		infereMeanAndStdev(population, groupItems, expected);
		
		// Calculate zscore and pvalue
		zscore = (observed - expected.mean) / expected.stdev;
		
		leftPvalue = Probability.normal(zscore);
		rightPvalue = 1.0 - leftPvalue;
		twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
		twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;
		
		return new ZScoreResult(
				sampleSize,
				leftPvalue, rightPvalue, twoTailPvalue,
				observed, 
				expected.mean, expected.stdev, zscore);
	}

	protected abstract void infereMeanAndStdev(
			DoubleMatrix1D population, DoubleMatrix1D groupItems, PopulationStatistics expected);

}
