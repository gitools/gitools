package org.gitools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.DoubleParser;
import org.gitools.datafilters.ValueParser;
import org.gitools.model.Project;
import org.gitools.model.ToolConfig;
import org.gitools.model.analysis.Analysis;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.analysis.CsvAnalysisResource;
import org.gitools.persistence.analysis.REXmlAnalysisResource;
import org.gitools.stats.test.factory.TestFactory;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public abstract class AnalysisCommand implements Command {

	protected static final char defaultSep = '\t';
	protected static final char defaultQuote = '"';
	
	protected String title;
	protected String notes;
	
	@Deprecated
	protected String testName;
	
	protected ToolConfig testConfig;

	protected int samplingNumSamples;
	
	protected String dataFile;
	
	protected boolean dataBinaryCutoffEnabled;
	protected BinaryCutoff dataBinaryCutoff;
	
	@Deprecated
	protected ValueParser valueParser;
	
	protected String modulesFile;
	
	protected int minModuleSize;
	protected int maxModuleSize;
	
	protected boolean includeNonMappedItems;
	
	protected String workdir;
	
	protected String outputFormat;
	protected boolean resultsByCond;

	public AnalysisCommand(
			String analysisName, String testName, int samplingNumSamples, 
			String dataFile, ValueParser valueParser, 
			String groupsFile, int minGroupSize, int maxGroupSize, 
			boolean includeNonMappedItems, 
			String workdir, String outputFormat, boolean resultsByCond) {
		
		this.title = analysisName;
		this.testName = testName;
		this.samplingNumSamples = samplingNumSamples;
		this.dataFile = dataFile;
		this.valueParser = valueParser != null ? valueParser : new DoubleParser();
		this.modulesFile = groupsFile;
		this.minModuleSize = minGroupSize;
		this.maxModuleSize = maxGroupSize;
		this.includeNonMappedItems = includeNonMappedItems;
		this.workdir = workdir;
		this.outputFormat = outputFormat;
		this.resultsByCond = resultsByCond;
	}

	public AnalysisCommand() {
		samplingNumSamples = 10000;
		dataBinaryCutoffEnabled = false;
		valueParser = new DoubleParser();
		minModuleSize = 0;
		maxModuleSize = Integer.MAX_VALUE;
		includeNonMappedItems = true;
		outputFormat = "csv";
		resultsByCond = true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;	
	}
	
	@Deprecated // Use testConfig instead
	public String getTestName() {
		return testName;
	}

	@Deprecated // Use testConfig instead
	public void setTestName(String testName) {
		this.testName = testName;
	}

	public ToolConfig getTestConfig() {
		return testConfig;
	}
	
	public void setTestConfig(ToolConfig testConfig) {
		this.testConfig = testConfig;
	}
	
	public int getSamplingNumSamples() {
		return samplingNumSamples;
	}

	public void setSamplingNumSamples(int samplingNumSamples) {
		this.samplingNumSamples = samplingNumSamples;
	}

	public String getDataFile() {
		return dataFile;
	}

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	@Deprecated // Use dataBinaryCutoff* instead
	public ValueParser getValueParser() {
		return valueParser;
	}

	@Deprecated // Use dataBinaryCutoff* instead
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	
	public boolean getDataBinaryCutoffEnabled() {
		return dataBinaryCutoffEnabled;
	}
	
	public void setDataBinaryCutoffEnabled(boolean dataBinaryCutoffEnabled) {
		this.dataBinaryCutoffEnabled = dataBinaryCutoffEnabled;
	}

	public BinaryCutoff getDataBinaryCutoff() {
		return dataBinaryCutoff;
	}
	
	public void setDataBinaryCutoff(BinaryCutoff dataBinaryCutoff) {
		this.dataBinaryCutoff = dataBinaryCutoff;
	}
	
	public String getModulesFile() {
		return modulesFile;
	}

	public void setModulesFile(String modulesFile) {
		this.modulesFile = modulesFile;
	}

	public int getMinModuleSize() {
		return minModuleSize;
	}

	public void setMinModuleSize(int minModuleSize) {
		this.minModuleSize = minModuleSize;
	}

	public int getMaxModuleSize() {
		return maxModuleSize;
	}

	public void setMaxModuleSize(int maxModuleSize) {
		this.maxModuleSize = maxModuleSize;
	}

	public boolean isIncludeNonMappedItems() {
		return includeNonMappedItems;
	}

	public void setIncludeNonMappedItems(boolean includeNonMappedItems) {
		this.includeNonMappedItems = includeNonMappedItems;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
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

	protected TestFactory createTestFactory(String toolName, String configName) {		
		ToolConfig toolConfig =
			TestFactory.createToolConfig(toolName, configName);
		
		TestFactory testFactory = 
			TestFactory.createFactory(toolConfig);
		
		return testFactory;
	}
	
	protected void save(Analysis analysis, IProgressMonitor monitor) 
			throws PersistenceException {

		final String basePath = workdir + File.separator + title;
		
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
			else if ("rexml".equalsIgnoreCase(format))
				ar = new REXmlAnalysisResource(basePath, minModuleSize, maxModuleSize);
			
			ar.save(analysis, monitor.subtask());
		}
		
		monitor.end();
	}

	private void saveProject(
			String basePath, Analysis analysis, IProgressMonitor monitor) 
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
