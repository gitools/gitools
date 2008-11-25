package es.imim.bg.ztools.command;

import java.util.HashMap;
import java.util.Map;

import es.imim.bg.ztools.stats.calc.MeanStatisticCalc;
import es.imim.bg.ztools.stats.calc.MedianStatisticCalc;
import es.imim.bg.ztools.test.BinomialTest.AproximationMode;
import es.imim.bg.ztools.test.factory.BinomialTestFactory;
import es.imim.bg.ztools.test.factory.FisherTestFactory;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.factory.ZscoreWithSamplingTestFactory;

public abstract class AnalysisCommand implements Command {

	protected static final char defaultSep = '\t';
	protected static final char defaultQuote = '"';
	
	protected static enum TestEnum {
		zscoreMean, zscoreMedian, 
		binomial, binomialExact, binomialNormal, binomialPoisson,
		hypergeometric, fisherExact, chiSquare
	}
	
	protected String analysisName;
	
	protected String testName;

	protected int samplingNumSamples;
	
	protected String dataFile;
	
	protected String modulesFile;
	
	protected int minModuleSize;
	protected int maxModuleSize;
	
	protected String workdir;
	
	protected String outputFormat;
	protected boolean resultsByCond;

	public AnalysisCommand(
			String analysisName, String testName, int samplingNumSamples, 
			String dataFile, String groupsFile,
			int minGroupSize, int maxGroupSize,
			String workdir, String outputFormat, boolean resultsByCond) {
		
		this.analysisName = analysisName;
		this.testName = testName;
		this.samplingNumSamples = samplingNumSamples;
		this.dataFile = dataFile;
		this.modulesFile = groupsFile;
		this.minModuleSize = minGroupSize;
		this.maxModuleSize = maxGroupSize;
		this.workdir = workdir;
		this.outputFormat = outputFormat;
		this.resultsByCond = resultsByCond;
	}
	
	protected TestFactory createTestFactory(String testName) {
		TestFactory testFactory = null;
		
		Map<String, TestEnum> testAliases = 
			new HashMap<String, TestEnum>();
		
		testAliases.put("zscore", TestEnum.zscoreMean);
		testAliases.put("zscore-mean", TestEnum.zscoreMean);
		testAliases.put("zscore-median", TestEnum.zscoreMedian);
		testAliases.put("binomial", TestEnum.binomial);
		testAliases.put("binomial-exact", TestEnum.binomialExact);
		testAliases.put("binomial-normal", TestEnum.binomialNormal);
		testAliases.put("binomial-poisson", TestEnum.binomialPoisson);
		testAliases.put("fisher", TestEnum.fisherExact);
		testAliases.put("hyper-geom", TestEnum.hypergeometric);
		testAliases.put("hyper-geometric", TestEnum.hypergeometric);
		testAliases.put("hypergeometric", TestEnum.hypergeometric);
		testAliases.put("chi-square", TestEnum.chiSquare);
		
		TestEnum selectedTest = testAliases.get(testName);
		if (selectedTest == null)
			throw new IllegalArgumentException("Unknown test " + testName);
		
		switch (selectedTest) {
		case zscoreMean:
			testFactory = new ZscoreWithSamplingTestFactory(
					samplingNumSamples, new MeanStatisticCalc());
			break;
		case zscoreMedian:
			testFactory = new ZscoreWithSamplingTestFactory(
					samplingNumSamples, new MedianStatisticCalc());
			break;
		case binomial:
			testFactory = new BinomialTestFactory(
					AproximationMode.automatic);
			break;
		case binomialExact:
			testFactory = new BinomialTestFactory(
					AproximationMode.onlyExact);
			break;
		case binomialNormal:
			testFactory = new BinomialTestFactory(
					AproximationMode.onlyNormal);
			break;
		case binomialPoisson:
			testFactory = new BinomialTestFactory(
					AproximationMode.onlyPoisson);
			break;
		case hypergeometric:
			throw new IllegalArgumentException("Test not implemented yet: " + testName);
			//break;
		case fisherExact:
			testFactory = new FisherTestFactory();
			break;
		case chiSquare:
			throw new IllegalArgumentException("Test not implemented yet: " + testName);
			//break;
		}
		
		return testFactory;
	}
}
