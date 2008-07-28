package es.imim.bg.ztools.zcalc;

import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.zcalc.analysis.ZCalcAnalysis;
import es.imim.bg.ztools.zcalc.input.ZCalcInput;
import es.imim.bg.ztools.zcalc.method.factory.FisherZCalcMethodFactory;
import es.imim.bg.ztools.zcalc.method.factory.PoissonZCalcMethodFactory;
import es.imim.bg.ztools.zcalc.method.factory.ZscoreBinomialZCalcMethodFactory;
import es.imim.bg.ztools.zcalc.method.factory.ZscoreWithSamplingZCalcMethodFactory;
import es.imim.bg.ztools.zcalc.method.factory.ZCalcMethodFactory;
import es.imim.bg.ztools.zcalc.output.CsvZCalcOutput;
import es.imim.bg.ztools.zcalc.output.REXmlZCalcOutput;
import es.imim.bg.ztools.zcalc.output.TabZCalcOutput;
import es.imim.bg.ztools.zcalc.output.ZCalcOutput;
import es.imim.bg.ztools.zcalc.statcalc.MeanStatisticCalc;
import es.imim.bg.ztools.zcalc.statcalc.MedianStatisticCalc;

public class ZCalcCommand {

	private static final char defaultSep = '\t';
	private static final char defaultQuote = '"';
	
	private String analysisName;
	
	private String methodName;

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

	public ZCalcCommand(
			String analysisName, String methodName, int samplingNumSamples, 
			String dataFile, char dataSep, char dataQuote, 
			String groupsFile, char groupsSep, char groupsQuote,
			int minGroupSize, int maxGroupSize,
			String workdir, String outputFormat) {
		
		this.analysisName = analysisName;
		this.methodName = methodName;
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
	}

	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		monitor.begin("Loading input data ...", 1);
		
		ZCalcInput in = new ZCalcInput(
				dataFile, dataSep, dataQuote,
				groupsFile, groupsSep, groupsQuote,
				null, '\t', '"', minGroupSize, maxGroupSize);
		
		in.load(monitor.subtask());
		
		monitor.end();
		
		ZCalcMethodFactory methodFactory = null;
		
		if (methodName.equalsIgnoreCase("zscore-mean"))
			methodFactory = new ZscoreWithSamplingZCalcMethodFactory(
					samplingNumSamples, new MeanStatisticCalc());
		else if (methodName.equalsIgnoreCase("zscore-median"))
			methodFactory = new ZscoreWithSamplingZCalcMethodFactory(
					samplingNumSamples, new MedianStatisticCalc());
		else if (methodName.equalsIgnoreCase("zscore-binomial"))
			methodFactory = new ZscoreBinomialZCalcMethodFactory();
		else if (methodName.equalsIgnoreCase("poisson"))
			methodFactory = new PoissonZCalcMethodFactory();
		else if (methodName.equalsIgnoreCase("fisher"))
			methodFactory = new FisherZCalcMethodFactory();
		else
			throw new IllegalArgumentException("Unknown method " + methodName);
		
		ZCalcAnalysis analysis = 
			new ZCalcAnalysis(
				analysisName,
				in.getPropNames(), in.getItemNames(), in.getData(), 
				in.getGroupNames(), in.getGroupItemIndices(),
				methodFactory);
		
		analysis.run(monitor);
		
		monitor.begin("Saving results in '" + workdir + "'...", 1);
		
		ZCalcOutput output = null;
		
		if (outputFormat.equalsIgnoreCase("csv"))
			output = new TabZCalcOutput(workdir, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("csv-sep"))
			output = new CsvZCalcOutput(workdir, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("rexml"))
			output = new REXmlZCalcOutput(workdir, minGroupSize, maxGroupSize);
		else
			throw new IllegalArgumentException("Unknown output format '" + outputFormat + "'");
		
		output.save(analysis);
		
		monitor.end();
	}

}
