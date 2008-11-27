package es.imim.bg.ztools.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.TestConfig;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.REXmlAnalysisResource;
import es.imim.bg.ztools.resources.analysis.CsvAnalysisResource;
import es.imim.bg.ztools.resources.analysis.XmlAnalysisResource;
import es.imim.bg.ztools.test.factory.BinomialTestFactory;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.factory.ZscoreTestFactory;

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
		
		TestConfig config = null;
		
		switch (selectedTest) {
		case zscoreMean:
			config = new TestConfig(TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY, 
					String.valueOf(samplingNumSamples));
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY, 
					ZscoreTestFactory.MEAN_ESTIMATOR);
			break;
		case zscoreMedian:
			config = new TestConfig(TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY, 
					String.valueOf(samplingNumSamples));
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY, 
					ZscoreTestFactory.MEDIAN_ESTIMATOR);
			break;
		case binomial:
			config = new TestConfig(TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.AUTOMATIC_APROX);
			break;
		case binomialExact:
			config = new TestConfig(TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.EXACT_APROX);
			break;
		case binomialNormal:
			config = new TestConfig(TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.NORMAL_APROX);
			break;
		case binomialPoisson:
			config = new TestConfig(TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.POISSON_APROX);
			break;
		case hypergeometric:
			config = new TestConfig(TestFactory.HYPERGEOMETRIC_TEST);
			break;
		case fisherExact:
			config = new TestConfig(TestFactory.FISHER_EXACT_TEST);
			break;
		case chiSquare:
			config = new TestConfig(TestFactory.CHI_SQUARE_TEST);
			break;
		}
		
		TestFactory testFactory = 
			TestFactory.createFactory(config);
		
		return testFactory;
	}
	
	protected void save(Analysis analysis, ProgressMonitor monitor) 
			throws IOException, DataFormatException {

		final String basePath = workdir + File.separator + analysisName;
		
		monitor.begin("Saving analysis ...", 1);
		monitor.info("Location: " + basePath);
		
		Set<String> formats = new HashSet<String>();
		for (String format : outputFormat.split(","))
			formats.add(format);
		
		for (String format : formats) {
			AnalysisResource ar = null;
			
			if ("xml".equalsIgnoreCase(format))
				ar = new XmlAnalysisResource(basePath, resultsByCond);
			else if ("csv".equalsIgnoreCase(format))
				ar = new CsvAnalysisResource(basePath, resultsByCond);
			else if ("rexml".equalsIgnoreCase(format))
				ar = new REXmlAnalysisResource(basePath, minModuleSize, maxModuleSize);
			
			ar.save(analysis);
		}
		
		monitor.end();
	}
}
