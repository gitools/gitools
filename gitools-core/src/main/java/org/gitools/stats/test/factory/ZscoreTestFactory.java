package org.gitools.stats.test.factory;

import java.util.Map;

import org.gitools.model.ToolConfig;
import org.gitools.stats.calc.MeanStatistic;
import org.gitools.stats.calc.MedianStatistic;
import org.gitools.stats.calc.Statistic;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.ZscoreWithSamplingTest;


public final class ZscoreTestFactory extends TestFactory {

	public static final String NUM_SAMPLES_PROPERTY = "samples";
	public static final String ESTIMATOR_PROPERTY = "estimator";
	
	public static final String MEAN_ESTIMATOR = "mean";
	public static final String MEDIAN_ESTIMATOR = "median";
	
	public static final int DEFAULT_NUM_SAMPLES = 10000;
	
	private int numSamples;
	private Statistic statCalc;
	
	public ZscoreTestFactory(ToolConfig config) {
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
			this.numSamples = DEFAULT_NUM_SAMPLES;
		}
	}

	@Override
	public Test create() {
		return new ZscoreWithSamplingTest(numSamples, statCalc);
	}

}
