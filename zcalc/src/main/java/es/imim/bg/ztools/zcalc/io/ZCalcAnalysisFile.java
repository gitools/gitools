package es.imim.bg.ztools.zcalc.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

public class ZCalcAnalysisFile {
	
	private static final String tagAnalysisName = "name";
	private static final String tagMethodName = "method-name";
	private static final String tagResultNames = "result-names";
	private static final String tagStartTime = "start-time";
	private static final String tagElapsedTime = "elapsed-time";
	private static final String tagUserName = "user-name";
	
	private static final String dateFormat = "yyyy/MM/dd HH:mm:ss";
	
	protected char separator;
	protected char quote;
	
	protected String analysisName = "";
	protected String methodName = "";
	
	protected String[] resultNames = new String[0];
	
	protected Date startTime;
	protected long elapsedTime;
	
	protected String userName;
	
	public ZCalcAnalysisFile(char separator, char quote) {
		this.separator = separator;
		this.quote = quote;
	}

	public String getAnalysisName() {
		return analysisName;
	}

	public void setAnalysisName(String analysisName) {
		this.analysisName = analysisName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String[] getResultNames() {
		return resultNames;
	}
	
	public void setResultNames(String[] resultNames) {
		this.resultNames = resultNames;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void write(Writer writer) {
		RawCsvWriter out = new RawCsvWriter(writer, separator, quote);
		
		out.writeProperty(tagAnalysisName, analysisName);
		out.writeProperty(tagMethodName, methodName);
		out.writePropertyList(tagResultNames, resultNames);
		
		DateFormat df = new SimpleDateFormat(dateFormat);
		out.writeProperty(tagStartTime, df.format(startTime));
		
		out.writeProperty(tagElapsedTime, String.valueOf(elapsedTime));
		
		out.writeProperty(tagUserName, userName);
		
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
	
	public void read(Reader reader) throws IOException {
		
		CSVStrategy csvStrategy = new CSVStrategy(
				separator, quote, '#', true, true, true);
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		String[] line;
		while ((line = parser.getLine()) != null) {
			final String tag = line[0];
			if (tag.equals(tagAnalysisName) && line.length >= 2)
				analysisName = line[1];
			else if (tag.equals(tagMethodName) && line.length >= 2)
				methodName = line[1];
			else if (tag.equals(tagResultNames) && line.length >= 2) {
				resultNames = new String[line.length - 1];
				System.arraycopy(line, 1, resultNames, 0, resultNames.length);
			}
			else if (tag.equals(tagElapsedTime) && line.length >= 2)
				try {
					startTime = new SimpleDateFormat(dateFormat).parse(line[1]);
				} catch (ParseException e) { }
			else if (tag.equals(tagElapsedTime) && line.length >= 2)
				elapsedTime = Long.parseLong(line[1]);
			else if (tag.equals(tagUserName) && line.length >= 2)
				userName = line[1];
		}
	}
}
