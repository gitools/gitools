package es.imim.bg.ztools.zcalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Data;
import es.imim.bg.ztools.model.Modules;
import es.imim.bg.ztools.resources.DataFile;
import es.imim.bg.ztools.resources.ModulesFile;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.REXmlAnalysisResource;
import es.imim.bg.ztools.resources.analysis.TabAnalysisResource;
import es.imim.bg.ztools.stats.calc.MeanStatisticCalc;
import es.imim.bg.ztools.stats.calc.MedianStatisticCalc;
import es.imim.bg.ztools.test.BinomialTest.AproximationMode;
import es.imim.bg.ztools.test.factory.BinomialTestFactory;
import es.imim.bg.ztools.test.factory.FisherTestFactory;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.factory.ZscoreWithSamplingTestFactory;

public class ZCalcCommand {

	private static enum TestEnum {
		zscoreMean, zscoreMedian, 
		binomial, binomialExact, binomialNormal, binomialPoisson,
		hypergeometric, fisherExact, chiSquare
	}
	
	private static final char defaultSep = '\t';
	private static final char defaultQuote = '"';
	
	private String analysisName;
	
	private String testName;

	private int samplingNumSamples;
	
	private String dataFile;
	
	private String groupsFile;
	
	private int minGroupSize;
	private int maxGroupSize;
	
	private String workdir;
	
	private String outputFormat;
	private boolean resultsByCond;

	public ZCalcCommand(
			String analysisName, String testName, int samplingNumSamples, 
			String dataFile, String groupsFile,
			int minGroupSize, int maxGroupSize,
			String workdir, String outputFormat, boolean resultsByCond) {
		
		this.analysisName = analysisName;
		this.testName = testName;
		this.samplingNumSamples = samplingNumSamples;
		this.dataFile = dataFile;
		this.groupsFile = groupsFile;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
		this.workdir = workdir;
		this.outputFormat = outputFormat;
		this.resultsByCond = resultsByCond;
	}

	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare output
		
		AnalysisResource output = createOutput(outputFormat);
		
		// Prepare test factory
		
		TestFactory testFactory = createTestFactory(testName);
		
		// Load data and modules
		
		monitor.begin("Loading input data ...", 1);
		
		Data data = new Data();
		Modules modules = new Modules();
		loadDataAndModules(
				data, modules, 
				dataFile, groupsFile, 
				minGroupSize, maxGroupSize,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setName(analysisName);
		analysis.setTestFactory(testFactory);
		analysis.setData(data);
		analysis.setModules(modules);
		
		ZCalcProcessor processor = 
			new ZCalcProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		monitor.begin("Saving analysis in '" + workdir + File.separator + analysisName + "'...", 1);
	
		output.save(analysis);
		
		monitor.end();
	}

	private void loadDataAndModules(
			Data data, Modules modules,
			String dataFileName, String modulesFileName, 
			int minModuleSize, int maxModuleSize, 
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataFile dataFile = new DataFile(dataFileName);
		dataFile.loadMetadata(data, monitor);
		
		// Load modules
		
		ModulesFile modulesFile = new ModulesFile(modulesFileName);
		modulesFile.load(
			modules,
			minGroupSize,
			maxGroupSize,
			data.getRowNames(),
			monitor);
		
		// Load data
		
		dataFile.loadData(
				data,
				null, 
				modules.getItemsOrder(), 
				monitor);
		
	}

	private AnalysisResource createOutput(String outputFormat) {
		AnalysisResource output = null;
		
		if (outputFormat.equalsIgnoreCase("csv"))
			output = new TabAnalysisResource(workdir, resultsByCond, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("rexml"))
			output = new REXmlAnalysisResource(workdir, minGroupSize, maxGroupSize);
		else
			throw new IllegalArgumentException("Unknown output format '" + outputFormat + "'");
		
		return output;
	}

	private TestFactory createTestFactory(String testName) {
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
