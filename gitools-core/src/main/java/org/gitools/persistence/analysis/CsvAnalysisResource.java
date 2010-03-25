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
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextSimplePersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;

import edu.upf.bg.csv.RawCsvWriter;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.matrix.model.IMatrix;

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
	public HtestAnalysis load(IProgressMonitor monitor)
			throws PersistenceException {

	    HtestAnalysis analysis = new HtestAnalysis();
		load(analysis, monitor);
		return analysis;
	}
	
	public void load(HtestAnalysis analysis, IProgressMonitor monitor)
			throws PersistenceException {
		
		try {
			File path = new File(basePath, descFileName);
			Reader reader = PersistenceUtils.openReader(path);
			CSVParser parser = new CSVParser(reader, csvStrategy);
			
			analysis.setTestConfig(new ToolConfig());
			
			String version = null;
			
			String[] fields;
			while ((fields = parser.getLine()) != null) {
				final String tag = fields[0];
				if (tag.equals(tagVersion) && fields.length >= 2)
					version = fields[1];
				else if (tag.equals(tagAnalysisName) && fields.length >= 2)
					analysis.setTitle(fields[1]);
				else if (tag.equals(tagToolName) && fields.length >= 2)
					analysis.getTestConfig().setName(fields[1]);
				else if (tag.equals(tagToolProperty) && fields.length >= 3)
					analysis.getTestConfig().put(fields[1], fields[2]);
				else if (tag.equals(tagElapsedTime) && fields.length >= 2)
					try {
						analysis.setStartTime(new SimpleDateFormat(dateFormat).parse(fields[1]));
					} catch (ParseException e) { }
				else if (tag.equals(tagElapsedTime) && fields.length >= 2)
					analysis.setElapsedTime(Long.parseLong(fields[1]));
				else if (tag.equals(tagData) && fields.length >= 2) {
					File res = new File(basePath, fields[1]);
					DoubleMatrixTextPersistence per = new DoubleMatrixTextPersistence();
					DoubleMatrix doubleMatrix = per.read(res, monitor.subtask());
					analysis.setData(doubleMatrix);
				}
				else if (tag.equals(tagModules) && fields.length >= 2) {
					path = new File(basePath, fields[1]);
					ModuleMapTextSimplePersistence res = new ModuleMapTextSimplePersistence(path);
					//ModuleMap moduleMap = res.load(monitor.subtask());
					//FIXME analysis.setModuleMap(moduleMap);
				}
				else if (tag.equals(tagResults) && fields.length >= 2) {
					File res = new File(basePath, fields[1]);
					ObjectMatrixTextPersistence per = new ObjectMatrixTextPersistence();
					ObjectMatrix resultsMatrix = per.read(res, monitor.subtask());
					analysis.setResults(resultsMatrix);
				}
				
				/*else if (tag.equals(tagUserName) && line.length >= 2)
					userName = line[1];*/
			}
	
			if (version == null && analysis.getResults() == null) { // old version
				File res = new File(basePath, "results.csv");
				ObjectMatrixTextPersistence per = new ObjectMatrixTextPersistence();
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
			HtestAnalysis analysis,
			IProgressMonitor monitor)
			throws PersistenceException {
	
		monitor.begin("Saving analysis in csv format...", 1);
		
		try {
			String dirName = basePath /*+ File.separator + analysis.getName()*/;
			File workDirFile = new File(dirName);
			if (!workDirFile.exists())
				workDirFile.mkdirs();
			
			TestFactory testFactory = 
				TestFactory.createFactory(analysis.getTestConfig());
			
			Test test = testFactory.create(); //FIXME?

			IMatrix dataMatrix = analysis.getData();
			if (!(dataMatrix instanceof DoubleMatrix))
				throw new RuntimeException("This processor only works with DoubleMatrix data. "
						+ dataMatrix.getClass().getSimpleName() + " found instead.");

			DoubleMatrix doubleMatrix = (DoubleMatrix) dataMatrix;

			writeDescription(workDirFile, analysis, test);
			
			new DoubleMatrixTextPersistence().write(
					new File(workDirFile, dataFileName),
					doubleMatrix, monitor);

			//FIXME
			/*new ModuleMapTextSimplePersistence(new File(workDirFile, modulesFileName))
					.save(analysis.getModuleMap(), monitor);*/
			
			new ObjectMatrixTextPersistence().write(
					new File(workDirFile, resultsFileNamePrefix + ".cells.csv"),
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
			HtestAnalysis analysis,
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
		
		ToolConfig toolConfig = analysis.getTestConfig();
		
		out.writeProperty(tagToolName, toolConfig.getName());
		
		for (String name : toolConfig.getConfiguration().keySet())
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
