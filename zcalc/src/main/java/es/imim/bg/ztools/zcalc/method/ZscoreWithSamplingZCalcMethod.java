package es.imim.bg.ztools.zcalc.method;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.sampling.RandomSampler;
import es.imim.bg.ztools.zcalc.statcalc.StatisticCalc;

public class ZscoreWithSamplingZCalcMethod extends ZscoreZCalcMethod {

	protected int numSamples;
	
	public ZscoreWithSamplingZCalcMethod(int numSamples, StatisticCalc statCalc) {
		super(statCalc);
		this.numSamples = numSamples;
	}
	
	@Override
	protected void infereMeanAndStdev(
			DoubleMatrix1D population, DoubleMatrix1D groupItems, PopulationStatistics expected) {
		
		final int sampleSize = groupItems.size();
		
		long[] lindices = new long[sampleSize];
		int[] indices = new int[sampleSize];
		
		double sx = 0;
		double sx2 = 0;
		
		RandomEngine randomEngine = new MersenneTwister();
		
		for (int i = 0; i < numSamples; i++) {
			RandomSampler.sample(
					sampleSize, population.size(), 
					sampleSize, 0, lindices, 0, 
					randomEngine);
			
			copyIndices(lindices, indices);
			
			double xi = statCalc.calc(
					population.viewSelection(indices));
		
			sx += xi;
			sx2 += (xi * xi);
		}
		
		double N = numSamples;
		
		expected.mean = sx / N;
		expected.stdev = Math.sqrt((N * sx2) - (sx * sx)) / N;
			//Math.sqrt((sx2 - N * (expectedMean * expectedMean)) / (N - 1));
	}
	
	private final void copyIndices(long[] lindices, int[] indices) {
		for (int j = 0; j < lindices.length; j++)
			indices[j] = (int) lindices[j];
	}
}
