package org.gitools.analysis.htest;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.ToolConfig;
import org.gitools.stats.test.factory.TestFactory;

import org.gitools.commands.Command;
import org.gitools.datafilters.BinaryCutoffTranslator;

public abstract class HtestCommand implements Command {

	protected static final char defaultSep = '\t';
	protected static final char defaultQuote = '"';

	protected HtestAnalysis analysis;

	protected String dataMime;
	protected String dataPath;

	protected String workdir;

	protected String fileName;

	public HtestCommand(
			HtestAnalysis analysis,
			String dataMime,
			String dataFile,
			String workdir,
			String fileName) {

		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataFile;
		this.workdir = workdir;
		this.fileName = fileName;
	}

	public HtestAnalysis getaAnalysis() {
		return analysis;
	}

	public void setAnalysis(HtestAnalysis analysis) {
		this.analysis = analysis;
	}

	public String getDataMime() {
		return dataMime;
	}

	public void setDataMime(String dataMime) {
		this.dataMime = dataMime;
	}
	
	public String getDataFile() {
		return dataPath;
	}

	public void setDataFile(String dataFile) {
		this.dataPath = dataFile;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected ValueTranslator createValueParser(HtestAnalysis analysis) {
		return analysis.isBinaryCutoffEnabled() ?
			new BinaryCutoffTranslator(new BinaryCutoff(
				analysis.getBinaryCutoffCmp(),
				analysis.getBinaryCutoffValue())) :
			new DoubleTranslator();
	}
	
	protected TestFactory createTestFactory(String toolName, String configName) {		
		ToolConfig toolConfig =
			TestFactory.createToolConfig(toolName, configName);
		
		TestFactory testFactory = 
			TestFactory.createFactory(toolConfig);
		
		return testFactory;
	}
	
/*	protected void save(HtestAnalysis analysis, IProgressMonitor monitor)
			throws PersistenceException {

		final String basePath = workdir; //+ File.separator + analysis.getTitle();
		
		monitor.begin("Saving project ...", 1);
		monitor.info("Location: " + basePath);
		
		Set<String> formats = new HashSet<String>();
		for (String format : outputFormat.split(","))
			formats.add(format);
		formats.add("csv"); // to mantain compatibility
		
		//saveProject(basePath, analysis, monitor);
		
		for (String format : formats) {
			AnalysisPersistence ar = null;
			
			if ("csv".equalsIgnoreCase(format))
				ar = new CsvAnalysisResource(basePath, resultsByCond);
			else if ("rexml".equalsIgnoreCase(format) && analysis instanceof EnrichmentAnalysis) {
				EnrichmentAnalysis ea = (EnrichmentAnalysis) analysis;
				ar = new REXmlAnalysisResource(basePath,
						ea.getMinModuleSize(),
						ea.getMaxModuleSize());
			}
			
			ar.save(analysis, monitor.subtask());
		}
		
		monitor.end();
	}
*/
}
