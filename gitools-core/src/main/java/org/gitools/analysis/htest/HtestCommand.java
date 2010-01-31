package org.gitools.analysis.htest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.Project;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.analysis.CsvAnalysisResource;
import org.gitools.persistence.analysis.REXmlAnalysisResource;
import org.gitools.stats.test.factory.TestFactory;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
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

	protected String outputFormat;

	protected boolean resultsByCond;

	public HtestCommand(
			HtestAnalysis analysis,
			String dataMime,
			String dataFile,
			String workdir,
			String fileName,
			String outputFormat,
			boolean resultsByCond) {

		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataFile;
		this.workdir = workdir;
		this.fileName = fileName;
		this.outputFormat = outputFormat;
		this.resultsByCond = resultsByCond;
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

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public boolean isResultsByCond() {
		return resultsByCond;
	}

	public void setResultsByCond(boolean resultsByCond) {
		this.resultsByCond = resultsByCond;
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
	
	protected void save(HtestAnalysis analysis, IProgressMonitor monitor)
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

	private void saveProject(
			String basePath, HtestAnalysis analysis, IProgressMonitor monitor)
			throws FileNotFoundException, IOException {
		
		File path = new File(basePath);
		if (!path.exists())
			path.mkdirs();
		
		Project proj = new Project();
		//inv.setSummary("inv summary");
		//inv.setNotes("inv notes");
		// TODO
		//proj.getAnalysis().add(analysis);
		//proj.getDataTables().add(analysis.getDataTable());
		//proj.getModuleMaps().add(analysis.getModuleMap());
		
		//FIXME: 
//		ProjectPersistence res = new ProjectPersistence(path, "project.xml");
//		try {
//			res.save(proj, monitor.subtask());
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
	}
}
