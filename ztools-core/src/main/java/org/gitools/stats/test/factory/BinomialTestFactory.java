package org.gitools.stats.test.factory;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.BinomialTest;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.BinomialTest.AproximationMode;

public class BinomialTestFactory extends TestFactory {

	public static final String APROXIMATION_PROPERTY = "aproximation";
	
	public static final String EXACT_APROX = "exact";
	public static final String NORMAL_APROX = "normal";
	public static final String POISSON_APROX = "poisson";
	public static final String AUTOMATIC_APROX = "automatic";
	
	private AproximationMode aproxMode;
	
	public BinomialTestFactory(ToolConfig config) {
		super(config);
		
		final String aproxModeName = config.getProperties().get(APROXIMATION_PROPERTY);
		
		if ("exact".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyExact;
		else if ("normal".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyNormal;
		else if ("poisson".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.onlyPoisson;
		else if ("automatic".equalsIgnoreCase(aproxModeName))
			this.aproxMode = AproximationMode.automatic;
		else
			this.aproxMode = AproximationMode.onlyExact;
	}
	
	/*public BinomialTestFactory(AproximationMode aproxMode) {
		this.aproxMode = aproxMode;
	}*/
	
	@Override
	public Test create() {
		return new BinomialTest(aproxMode);
	}

}
