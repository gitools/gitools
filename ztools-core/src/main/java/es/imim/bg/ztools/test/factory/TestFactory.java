package es.imim.bg.ztools.test.factory;

import java.util.HashMap;
import java.util.Map;

import es.imim.bg.ztools.model.TestConfig;
import es.imim.bg.ztools.test.Test;

public abstract class TestFactory {

	public static final String ZSCORE_TEST = "zscore";
	public static final String BINOMIAL_TEST = "binomial";
	public static final String FISHER_EXACT_TEST = "fisher-exact";
	public static final String HYPERGEOMETRIC_TEST = "hypergeometric";
	public static final String CHI_SQUARE_TEST = "chi-square";
	
	public static enum TestEnum {
		zscore, binomial, hypergeometric, fisherExact, chiSquare
	}
	
	private static final Map<String, TestEnum> testNameMap =
		new HashMap<String, TestEnum>();
	
	static {
		testNameMap.put(ZSCORE_TEST, TestEnum.zscore);
		testNameMap.put(BINOMIAL_TEST, TestEnum.binomial);
		testNameMap.put(FISHER_EXACT_TEST, TestEnum.fisherExact);
		testNameMap.put(HYPERGEOMETRIC_TEST, TestEnum.hypergeometric);
		testNameMap.put(CHI_SQUARE_TEST, TestEnum.chiSquare);
	}
	
	public static TestFactory createFactory(TestConfig config) {
		
		final String testName = config.getName();
		TestEnum selectedTest = testNameMap.get(testName);
		if (selectedTest == null)
			throw new IllegalArgumentException("Unknown test " + testName);
		
		TestFactory testFactory = null;
		
		switch (selectedTest) {
		case zscore:
			testFactory = new ZscoreTestFactory(config);
			break;
		case binomial:
			testFactory = new BinomialTestFactory(config);
			break;
		case fisherExact:
			testFactory = new FisherTestFactory(config);
			break;
		case hypergeometric:
			throw new IllegalArgumentException("Test not implemented yet: " + testName);
			//break;
		case chiSquare:
			throw new IllegalArgumentException("Test not implemented yet: " + testName);
			//break;
		}
		
		return testFactory;
	}
	
	protected TestConfig testConfig;
	
	public TestFactory(TestConfig config) {
		this.testConfig = config;
	}
	
	public TestConfig getTestConfig() {
		return testConfig;
	}
	
	public abstract Test create();

}
