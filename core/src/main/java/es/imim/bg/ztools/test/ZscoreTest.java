package es.imim.bg.ztools.test;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;
import es.imim.bg.ztools.statcalc.StatisticCalc;
import es.imim.bg.ztools.test.results.Result;
import es.imim.bg.ztools.test.results.ZScoreResult;

public abstract class ZscoreTest extends AbstractTest {

	protected class PopulationStatistics {
		public double mean;
		public double stdev;
	}
	
	protected StatisticCalc statCalc;
	
	protected DoubleMatrix1D population;
	
	public ZscoreTest(StatisticCalc statCalc) {
		this.statCalc = statCalc;
	}
	
	@Override
	public String getName() {
		return "zscore-" + statCalc.getName();
	}

	@Override
	public String[] getResultNames() {
		return new ZScoreResult().getNames();
	}
	
	@Override
	public void processPopulation(String name, DoubleMatrix1D population) {
		this.population = population;
	}
	
	@Override
	public Result processTest(
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
		
		return new ZScoreResult(
				sampleSize,
				leftPvalue, rightPvalue, twoTailPvalue,
				observed, 
				expected.mean, expected.stdev, zscore);
	}

	protected abstract void infereMeanAndStdev(
			DoubleMatrix1D population, DoubleMatrix1D groupItems, PopulationStatistics expected);

}
