package es.imim.bg.ztools.zcalc.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.statcalc.MeanStatisticCalc;
import es.imim.bg.ztools.statcalc.MedianStatisticCalc;
import es.imim.bg.ztools.test.BinomialTest.AproximationMode;
import es.imim.bg.ztools.test.factory.BinomialTestFactory;
import es.imim.bg.ztools.test.factory.FisherTestFactory;
import es.imim.bg.ztools.test.factory.TestFactory;
import es.imim.bg.ztools.test.factory.ZscoreWithSamplingTestFactory;
import es.imim.bg.ztools.zcalc.analysis.ZCalcAnalysis;
import es.imim.bg.ztools.zcalc.input.FileZCalcInput;
import es.imim.bg.ztools.zcalc.input.ZCalcInput;
import es.imim.bg.ztools.zcalc.output.CsvZCalcOutput;
import es.imim.bg.ztools.zcalc.output.REXmlZCalcOutput;
import es.imim.bg.ztools.zcalc.output.TabZCalcOutput;
import es.imim.bg.ztools.zcalc.output.ZCalcOutput;

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
	
	private char dataSep = '\t';
	private char dataQuote = '"';
	
	private String groupsFile;
	
	private char groupsSep = '\t';
	private char groupsQuote = '"';
	
	private int minGroupSize;
	private int maxGroupSize;
	
	private String workdir;
	
	private String outputFormat;
	private boolean resultsByCond;

	public ZCalcCommand(
			String analysisName, String methodName, int samplingNumSamples, 
			String dataFile, char dataSep, char dataQuote, 
			String groupsFile, char groupsSep, char groupsQuote,
			int minGroupSize, int maxGroupSize,
			String workdir, String outputFormat, boolean resultsByCond) {
		
		this.analysisName = analysisName;
		this.testName = methodName;
		this.samplingNumSamples = samplingNumSamples;
		this.dataFile = dataFile;
		this.dataSep = dataSep;
		this.dataQuote = dataQuote;
		this.groupsFile = groupsFile;
		this.groupsSep = groupsSep;
		this.groupsQuote = groupsQuote;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
		this.workdir = workdir;
		this.outputFormat = outputFormat;
		this.resultsByCond = resultsByCond;
	}

	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare input
		
		ZCalcInput in = new FileZCalcInput(
				dataFile, groupsFile, minGroupSize, maxGroupSize);
		
		// Prepare output
		
		ZCalcOutput output = null;
		
		if (outputFormat.equalsIgnoreCase("csv"))
			output = new TabZCalcOutput(workdir, resultsByCond, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("csv-sep"))
			output = new CsvZCalcOutput(workdir, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("rexml"))
			output = new REXmlZCalcOutput(workdir, minGroupSize, maxGroupSize);
		else
			throw new IllegalArgumentException("Unknown output format '" + outputFormat + "'");
		
		// Prepare test factory
		
		TestFactory testFactory = null;
	
		Map<String, TestEnum> testAliases = new HashMap<String, TestEnum>();
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
		
		monitor.begin("Loading input data ...", 1);
		
		in.load(monitor.subtask());
		
		monitor.end();
		
		ZCalcAnalysis analysis = 
			new ZCalcAnalysis(
				analysisName,
				in.getCondNames(), in.getItemNames(), in.getData(), 
				in.getGroupNames(), in.getGroupItemIndices(),
				testFactory);
		
		analysis.run(monitor);
		
		monitor.begin("Saving results in '" + workdir + "'...", 1);
		
		output.save(analysis);
		
		monitor.end();
	}

}
