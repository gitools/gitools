package es.imim.bg.ztools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import javax.xml.bind.JAXBException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.datafilters.DoubleFilter;
import es.imim.bg.ztools.datafilters.ValueFilter;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Investigation;
import es.imim.bg.ztools.model.ToolConfig;
import es.imim.bg.ztools.resources.InvestigationResource;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.REXmlAnalysisResource;
import es.imim.bg.ztools.resources.analysis.CsvAnalysisResource;
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
	
	protected ValueFilter valueFilter;
	
	protected String modulesFile;
	
	protected int minModuleSize;
	protected int maxModuleSize;
	
	protected String workdir;
	
	protected String outputFormat;
	protected boolean resultsByCond;

	public AnalysisCommand(
			String analysisName, String testName, int samplingNumSamples, 
			String dataFile, ValueFilter valueFilter, 
			String groupsFile, int minGroupSize, int maxGroupSize,
			String workdir, String outputFormat, boolean resultsByCond) {
		
		this.analysisName = analysisName;
		this.testName = testName;
		this.samplingNumSamples = samplingNumSamples;
		this.dataFile = dataFile;
		this.valueFilter = valueFilter != null ? valueFilter : new DoubleFilter();
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
		
		ToolConfig config = new ToolConfig();
		
		switch (selectedTest) {
		case zscoreMean:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY, 
					String.valueOf(samplingNumSamples));
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY, 
					ZscoreTestFactory.MEAN_ESTIMATOR);
			break;
		case zscoreMedian:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY, 
					String.valueOf(samplingNumSamples));
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY, 
					ZscoreTestFactory.MEDIAN_ESTIMATOR);
			break;
		case binomial:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.AUTOMATIC_APROX);
			break;
		case binomialExact:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.EXACT_APROX);
			break;
		case binomialNormal:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.NORMAL_APROX);
			break;
		case binomialPoisson:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY, 
					BinomialTestFactory.POISSON_APROX);
			break;
		case hypergeometric:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.HYPERGEOMETRIC_TEST);
			break;
		case fisherExact:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.FISHER_EXACT_TEST);
			break;
		case chiSquare:
			config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.CHI_SQUARE_TEST);
			break;
		}
		
		TestFactory testFactory = 
			TestFactory.createFactory(config);
		
		return testFactory;
	}
	
	protected void save(Analysis analysis, ProgressMonitor monitor) 
			throws IOException, DataFormatException {

		final String basePath = workdir + File.separator + analysisName;
		
		monitor.begin("Saving investigation ...", 1);
		monitor.info("Location: " + basePath);
		
		Set<String> formats = new HashSet<String>();
		for (String format : outputFormat.split(","))
			formats.add(format);
		formats.add("csv"); // to mantain compatibility
		
		saveInvestigation(basePath, analysis, monitor);
		
		for (String format : formats) {
			AnalysisResource ar = null;
			
			if ("csv".equalsIgnoreCase(format))
				ar = new CsvAnalysisResource(basePath, resultsByCond);
			else if ("rexml".equalsIgnoreCase(format))
				ar = new REXmlAnalysisResource(basePath, minModuleSize, maxModuleSize);
			
			ar.save(analysis, monitor.subtask());
		}
		
		monitor.end();
	}

	private void saveInvestigation(
			String basePath, Analysis analysis, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException {
		
		File path = new File(basePath);
		if (!path.exists())
			path.mkdirs();
		
		Investigation inv = new Investigation();
		//inv.setSummary("inv summary");
		//inv.setNotes("inv notes");
		inv.getAnalysis().add(analysis);
		inv.getDataMatrices().add(analysis.getDataMatrix());
		inv.getModuleSets().add(analysis.getModuleSet());
		InvestigationResource res = new InvestigationResource(
				new File(path, "investigation.xml"));
		try {
			res.save(inv, monitor.subtask());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
