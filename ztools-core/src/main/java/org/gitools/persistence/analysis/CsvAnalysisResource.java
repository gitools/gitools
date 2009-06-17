package org.gitools.persistence.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.analysis.Analysis;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.SimpleMapPersistence;
import org.gitools.persistence.TextDoubleMatrixPersistence;
import org.gitools.persistence.TextObjectMatrixPersistence;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;

import edu.upf.bg.csv.RawCsvWriter;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class CsvAnalysisResource extends AnalysisPersistence {

	private static final long serialVersionUID = 5332483819895529191L;

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
	
	@Deprecated
	public static final String resultsFileName = "results.csv.gz";
	
	public static final String resultsFileNamePrefix = "results";
	

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
	public Analysis load(IProgressMonitor monitor)
			throws PersistenceException {

	    Analysis analysis = new Analysis();
		load(analysis, monitor);
		return analysis;
	}
	
	public void load(Analysis analysis, IProgressMonitor monitor)
			throws PersistenceException {
		
		try {
			File path = new File(basePath, descFileName);
			Reader reader = FileResource.openReader(path);
			CSVParser parser = new CSVParser(reader, csvStrategy);
			
			analysis.setToolConfig(new ToolConfig());
			
			String version = null;
			
			String[] fields;
			while ((fields = parser.getLine()) != null) {
				final String tag = fields[0];
				if (tag.equals(tagVersion) && fields.length >= 2)
					version = fields[1];
				else if (tag.equals(tagAnalysisName) && fields.length >= 2)
					analysis.setTitle(fields[1]);
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
					IResource res = new FileResource(new File(basePath, fields[1]));
					TextDoubleMatrixPersistence per = new TextDoubleMatrixPersistence();
					DoubleMatrix doubleMatrix = per.read(res, monitor.subtask());
					analysis.setDataTable(doubleMatrix);
				}
				else if (tag.equals(tagModules) && fields.length >= 2) {
					path = new File(basePath, fields[1]);
					SimpleMapPersistence res = new SimpleMapPersistence(path);
					ModuleMap moduleMap = res.load(monitor.subtask());
					analysis.setModuleSet(moduleMap);
				}
				else if (tag.equals(tagResults) && fields.length >= 2) {
					IResource res = new FileResource(new File(basePath, fields[1]));
					TextObjectMatrixPersistence per = new TextObjectMatrixPersistence();
					ObjectMatrix resultsMatrix = per.read(res, monitor.subtask());
					analysis.setResults(resultsMatrix);
				}
				
				/*else if (tag.equals(tagUserName) && line.length >= 2)
					userName = line[1];*/
			}
	
			if (version == null && analysis.getResults() == null) { // old version
				IResource res = new FileResource(new File(basePath, "results.csv"));
				TextObjectMatrixPersistence per = new TextObjectMatrixPersistence();
				ObjectMatrix resultsMatrix = per.read(res, monitor.subtask());
				analysis.setResults(resultsMatrix);
			}
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public void save(
			Analysis analysis,
			IProgressMonitor monitor)
			throws PersistenceException {
	
		monitor.begin("Saving analysis in csv format...", 1);
		
		try {
			String dirName = basePath /*+ File.separator + analysis.getName()*/;
			File workDirFile = new File(dirName);
			if (!workDirFile.exists())
				workDirFile.mkdirs();
			
			TestFactory testFactory = 
				TestFactory.createFactory(analysis.getToolConfig());
			
			Test test = testFactory.create(); //FIXME?
			
			writeDescription(workDirFile, analysis, test);
			
			new TextDoubleMatrixPersistence().write(
					new FileResource(new File(workDirFile, dataFileName)),
					analysis.getDataTable(), monitor);
			
			new SimpleMapPersistence(new File(workDirFile, modulesFileName))
					.save(analysis.getModuleMap(), monitor);
			
			new TextObjectMatrixPersistence().write(
					new FileResource(new File(workDirFile, resultsFileNamePrefix + ".cells.csv")),
					analysis.getResults(), 
					resultsOrderByCond, monitor);
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		monitor.end();
	}

	protected void writeDescription(
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
		
		out.writeProperty(tagAnalysisName, analysis.getTitle());
		
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
		
		out.writeProperty(tagResults, resultsFileNamePrefix + ".cells.tsv.gz");
		
		out.close();
	}
}
