package es.imim.bg.ztools.resources.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.model.ModuleSet;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.model.ToolConfig;
import es.imim.bg.ztools.resources.DataResource;
import es.imim.bg.ztools.resources.ModulesResource;
import es.imim.bg.ztools.resources.Resource;
import es.imim.bg.ztools.resources.ResultsResource;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;

public class CsvAnalysisResource extends AnalysisResource {

	private static final String tagVersion = "version";
	private static final String tagAnalysisName = "name";
	private static final String tagToolName = "tool";
	private static final String tagToolProperty = "tool-property";
	private static final String tagStartTime = "start-time";
	private static final String tagElapsedTime = "elapsed-time";
	private static final String tagUserName = "user-name";
	private static final String tagData = "data";
	private static final String tagModules = "modules";
	private static final String tagResults = "results";
	
	private static final String dateFormat = "yyyy/MM/dd HH:mm:ss";
	
	public static final String descFileName = "analysis.csv";
	public static final String dataFileName = "data.csv.gz";
	public static final String modulesFileName = "modules.csv.gz";
	public static final String resultsFileName = "results.csv.gz";

	final CSVStrategy csvStrategy = new CSVStrategy(
			'\t', '"', '#', true, true, true);
	
	protected boolean resultsOrderByCond;
	
	public CsvAnalysisResource(String basePath, boolean resultsOrderByCond) {
		super(basePath);
		this.resultsOrderByCond = resultsOrderByCond;
	}
	
	public CsvAnalysisResource(String basePath) {
		this(basePath, true);
	}

	public void setResultsOrderByCond(boolean resultsOrderByCond) {
		this.resultsOrderByCond = resultsOrderByCond;
	}
	
	@Override
	public Analysis load(ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		Analysis analysis = new Analysis();
		load(analysis, monitor);
		return analysis;
	}
	
	public void load(Analysis analysis, ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		File path = new File(basePath, descFileName);
		Reader reader = Resource.openReader(path);
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		analysis.setToolConfig(new ToolConfig());
		
		String version = null;
		
		String[] fields;
		while ((fields = parser.getLine()) != null) {
			final String tag = fields[0];
			if (tag.equals(tagVersion) && fields.length >= 2)
				version = fields[1];
			else if (tag.equals(tagAnalysisName) && fields.length >= 2)
				analysis.setName(fields[1]);
			else if (tag.equals(tagToolName) && fields.length >= 2)
				analysis.getToolConfig().setName(fields[1]);
			else if (tag.equals(tagToolProperty) && fields.length >= 3)
				analysis.getToolConfig().put(fields[1], fields[2]);
			else if (tag.equals(tagElapsedTime) && fields.length >= 2)
				try {
					analysis.setStartTime(new SimpleDateFormat(dateFormat).parse(fields[1]));
				} catch (ParseException e) { }
			else if (tag.equals(tagElapsedTime) && fields.length >= 2)
				analysis.setElapsedTime(Long.parseLong(fields[1]));
			else if (tag.equals(tagData) && fields.length >= 2) {
				path = new File(basePath, fields[1]);
				DataResource res = new DataResource(path);
				DataMatrix dataMatrix = res.load(monitor.subtask());
				analysis.setDataMatrix(dataMatrix);
			}
			else if (tag.equals(tagModules) && fields.length >= 2) {
				path = new File(basePath, fields[1]);
				ModulesResource res = new ModulesResource(path);
				ModuleSet moduleSet = res.load(monitor.subtask());
				analysis.setModuleSet(moduleSet);
			}
			else if (tag.equals(tagResults) && fields.length >= 2) {
				path = new File(basePath, fields[1]);
				ResultsResource resFile = new ResultsResource(path);
				ResultsMatrix resultsMatrix = resFile.read(monitor.subtask());
				analysis.setResults(resultsMatrix);
			}
			
			/*else if (tag.equals(tagUserName) && line.length >= 2)
				userName = line[1];*/
		}

		if (version == null && analysis.getResults() == null) { // old version
			path = new File(basePath, "results.csv");
			ResultsResource resFile = new ResultsResource(path);
			ResultsMatrix resultsMatrix = resFile.read(monitor.subtask());
			analysis.setResults(resultsMatrix);
		}
	}
	
	@Override
	public void save(Analysis analysis, ProgressMonitor monitor) throws IOException {
	
		monitor.begin("Saving analysis in csv format...", 1);
		
		String dirName = basePath /*+ File.separator + analysis.getName()*/;
		File workDirFile = new File(dirName);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getToolConfig());
		
		Test test = testFactory.create(); //FIXME?
		
		saveDescription(workDirFile, analysis, test);
		
		new DataResource(new File(workDirFile, dataFileName))
			.save(analysis.getDataMatrix(), monitor);
		
		new ModulesResource(new File(workDirFile, modulesFileName))
			.save(analysis.getModuleSet(), monitor);
		
		new ResultsResource(new File(workDirFile, resultsFileName))
			.write(analysis.getResults(), resultsOrderByCond, monitor);
		
		monitor.end();
	}

	protected void saveDescription(
			File workDirFile, 
			Analysis analysis,
			Test method) throws IOException {
		
		Writer writer = new FileWriter(new File(
						workDirFile, 
						descFileName));
		
		RawCsvWriter out = new RawCsvWriter(
				writer, 
				csvStrategy.getDelimiter(), 
				csvStrategy.getEncapsulator());
		
		out.writeProperty(tagVersion, 
				getClass().getPackage().getImplementationVersion());
		
		out.writeProperty(tagAnalysisName, analysis.getName());
		
		ToolConfig toolConfig = analysis.getToolConfig();
		
		out.writeProperty(tagToolName, toolConfig.getName());
		
		for (String name : toolConfig.getProperties().keySet())
			out.writePropertyList(tagToolProperty, new String[] {
					name, toolConfig.get(name) });
		
		//out.writePropertyList(tagResultNames, resultNames);
		
		DateFormat df = new SimpleDateFormat(dateFormat);
		out.writeProperty(tagStartTime, 
				df.format(analysis.getStartTime()));
		
		out.writeProperty(tagElapsedTime, 
				String.valueOf(analysis.getElapsedTime()));
		
		out.writeProperty(tagUserName, 
				System.getProperty("user.name"));
		
		out.writeProperty(tagData, dataFileName);
		out.writeProperty(tagModules, modulesFileName);
		out.writeProperty(tagResults, resultsFileName);
		
		out.close();
	}
}
