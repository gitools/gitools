package es.imim.bg.ztools.test.factory;

import java.util.Map;

import es.imim.bg.ztools.model.TestConfig;
import es.imim.bg.ztools.stats.calc.MeanStatistic;
import es.imim.bg.ztools.stats.calc.MedianStatistic;
import es.imim.bg.ztools.stats.calc.Statistic;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.ZscoreWithSamplingTest;

public final class ZscoreTestFactory extends TestFactory {

	public static final String NUM_SAMPLES_PROPERTY = "samples";
	public static final String ESTIMATOR_PROPERTY = "estimator";
	
	public static final String MEAN_ESTIMATOR = "mean";
	public static final String MEDIAN_ESTIMATOR = "median";
	
	private int numSamples;
	private Statistic statCalc;
	
	public ZscoreTestFactory(TestConfig config) {
		super(config);
		
		Map<String, String> props = config.getProperties();
		
		final String estimatorName = props.get(ESTIMATOR_PROPERTY);
		if (MEAN_ESTIMATOR.equalsIgnoreCase(estimatorName))
			this.statCalc = new MeanStatistic();
		else if (MEDIAN_ESTIMATOR.equalsIgnoreCase(estimatorName))
			this.statCalc = new MedianStatistic();
		else
			this.statCalc = new MeanStatistic();
		
		try {
			this.numSamples = Integer.parseInt(props.get(NUM_SAMPLES_PROPERTY));
		}
		catch (NumberFormatException e) {
			this.numSamples = ZscoreWithSamplingTest.DEFAULT_NUM_SAMPLES;
		}
	}
	
	/*public ZscoreWithSamplingTestFactory(
			int numSamples,
			StatisticCalc statCalc) {
	
		this.numSamples = numSamples;
		this.statCalc = statCalc;
	}*/

	@Override
	public Test create() {
		return new ZscoreWithSamplingTest(numSamples, statCalc);
	}

}
