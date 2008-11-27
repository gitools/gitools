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
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.model.TestConfig;
import es.imim.bg.ztools.resources.Resource;
import es.imim.bg.ztools.resources.ResultsResource;
import es.imim.bg.ztools.test.Test;
import es.imim.bg.ztools.test.factory.TestFactory;

public class CsvAnalysisResource extends AnalysisResource {

	private static final String tagAnalysisName = "name";
	private static final String tagTestName = "test-name";
	private static final String tagTestProperty = "test-property";
	//private static final String tagResultNames = "result-names";
	private static final String tagStartTime = "start-time";
	private static final String tagElapsedTime = "elapsed-time";
	private static final String tagUserName = "user-name";
	private static final String tagResults = "results";
	
	private static final String dateFormat = "yyyy/MM/dd HH:mm:ss";
	
	public static final String descFileName = "analysis.csv";
	public static final String resultsFileName = "results.csv";

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
	public Analysis load() throws FileNotFoundException, IOException, DataFormatException {
		Analysis analysis = new Analysis();
		load(analysis);
		return analysis;
	}
	
	public void load(Analysis analysis) throws FileNotFoundException, IOException, DataFormatException {
		File path = new File(basePath, descFileName);
		Reader reader = Resource.openReader(path);
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		analysis.setTestConfig(new TestConfig());
		
		String[] fields;
		while ((fields = parser.getLine()) != null) {
			final String tag = fields[0];
			if (tag.equals(tagAnalysisName) && fields.length >= 2)
				analysis.setName(fields[1]);
			else if (tag.equals(tagTestName) && fields.length >= 2)
				analysis.getTestConfig().setName(fields[1]);
			else if (tag.equals(tagTestProperty) && fields.length >= 3)
				analysis.getTestConfig().put(fields[1], fields[2]);
			else if (tag.equals(tagElapsedTime) && fields.length >= 2)
				try {
					analysis.setStartTime(new SimpleDateFormat(dateFormat).parse(fields[1]));
				} catch (ParseException e) { }
			else if (tag.equals(tagElapsedTime) && fields.length >= 2)
				analysis.setElapsedTime(Long.parseLong(fields[1]));
			else if (tag.equals(tagResults) && fields.length >= 2) {
				path = new File(basePath, fields[1]);
				ResultsResource resFile = new ResultsResource(path);
				Results results = resFile.read();
				analysis.setResults(results);
			}
			/*else if (tag.equals(tagResultNames) && line.length >= 2) {
				resultNames = new String[line.length - 1];
				System.arraycopy(line, 1, resultNames, 0, resultNames.length);
			}
			else if (tag.equals(tagUserName) && line.length >= 2)
				userName = line[1];*/
		}
	}
	
	@Override
	public void save(Analysis analysis) throws IOException {
		
		String dirName = basePath /*+ File.separator + analysis.getName()*/;
		File workDirFile = new File(dirName);
		if (!workDirFile.exists())
			workDirFile.mkdirs();
		
		TestFactory testFactory = 
			TestFactory.createFactory(analysis.getTestConfig());
		
		Test test = testFactory.create(); //FIXME?
		
		saveDescription(workDirFile, analysis, test);
		
		saveResults(workDirFile, analysis);
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
		
		out.writeProperty(tagAnalysisName, analysis.getName());
		
		TestConfig testConfig = analysis.getTestConfig();
		
		out.writeProperty(tagTestName, 
				testConfig.getName());
		
		for (String name : testConfig.getProperties().keySet())
			out.writePropertyList(tagTestProperty,new String[] {
					name, testConfig.get(name) });
		
		//out.writePropertyList(tagResultNames, resultNames);
		
		DateFormat df = new SimpleDateFormat(dateFormat);
		out.writeProperty(tagStartTime, 
				df.format(analysis.getStartTime()));
		
		out.writeProperty(tagElapsedTime, 
				String.valueOf(analysis.getElapsedTime()));
		
		out.writeProperty(tagUserName, 
				System.getProperty("user.name"));
		
		out.writeProperty(tagResults, resultsFileName);
		
		/*out.writePropertyList("conditions", analysis.getPropNames());
		out.writePropertyList("items", analysis.getItemNames());
		
		int[][] groupItemIndices = analysis.getGroupItemIndices();
		String[] groupNames = analysis.getGroupNames();
		for (int groupIndex = 0; groupIndex < groupNames.length; groupIndex++) {
			final String groupName = groupNames[groupIndex];
			final int[] groupItems = groupItemIndices[groupIndex];
			out.writeValue("group");
			out.writeSeparator();
			out.writeQuotedValue(groupName);
			out.writeSeparator();
			//out.print(groupItems.length);
			for (int index : groupItems) {
				out.writeSeparator();
				out.writeValue(String.valueOf(index));
			}
			out.writeNewLine();
		}*/
		
		out.close();
	}
	
	protected void saveResults(
			File workDirFile, 
			Analysis analysis) throws IOException {
		
		/*Writer writer = new FileWriter(new File(
						workDirFile, 
						resultsFileName(analysis.getName())));*/
		
		File dest = new File(
				workDirFile, 
				resultsFileName);
		
		ResultsResource resFile = new ResultsResource(dest);
		resFile.write(analysis.getResults(), resultsOrderByCond);
	}
}
