package es.imim.bg.ztools.zcalc.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.ztools.zcalc.results.AbstractZCalcResult;

public class ZCalcResultsFile {
	
	protected char separator;
	protected char quote;
	
	private String[] propNames;
	private String[] groupNames;
	private String[] resultNames;
	private ObjectMatrix2D results;
	
	public ZCalcResultsFile(char separator, char quote) {
		this.separator = separator;
		this.quote = quote;
	}
	
	public String[] getPropNames() {
		return propNames;
	}

	public void setPropNames(String[] propNames) {
		this.propNames = propNames;
	}

	public String[] getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String[] groupNames) {
		this.groupNames = groupNames;
	}

	public String[] getResultNames() {
		return resultNames;
	}

	public void setResultNames(String[] resultNames) {
		this.resultNames = resultNames;
	}

	public ObjectMatrix2D getResults() {
		return results;
	}

	public void setResults(ObjectMatrix2D results) {
		this.results = results;
	}

	/*public void write(Writer writer) {
		write(writer, true);
	}*/
	
	public void write(Writer writer, boolean orderByCondition) {
		RawCsvWriter out = new RawCsvWriter(writer, separator, quote);
		
		out.writeQuotedValue("condition");
		out.writeSeparator();
		out.writeQuotedValue("module");
		for (String resultName : resultNames) {
			out.writeSeparator();
			out.writeQuotedValue(resultName);
		}
		
		out.writeNewLine();
		
		int numProperties = results.columns();
		int numGroups = results.rows();
		
		if (orderByCondition) {
			for (int propIndex = 0; propIndex < numProperties; propIndex++)
				for (int groupIndex = 0; groupIndex < numGroups; groupIndex++)
					writeLine(out, propIndex, groupIndex);
		}
		else {
			for (int groupIndex = 0; groupIndex < numGroups; groupIndex++)
				for (int propIndex = 0; propIndex < numProperties; propIndex++)
					writeLine(out, propIndex, groupIndex);
		}
		
		out.close();
	}

	private void writeLine(RawCsvWriter out, int propIndex, int groupIndex) {
		out.writeQuotedValue(propNames[propIndex]);
		out.writeSeparator();
		out.writeQuotedValue(groupNames[groupIndex]);
		
		AbstractZCalcResult cell = 
			(AbstractZCalcResult) results.getQuick(groupIndex, propIndex);
		
		Object[] values = cell.getValues();
		
		for (int paramIndex = 0; paramIndex < resultNames.length; paramIndex++) {
			out.writeSeparator();
			out.writeValue(String.valueOf(values[paramIndex]));
		}
		
		out.writeNewLine();
	}
	
	public void read(Reader reader) throws IOException, DataFormatException {
		
		CSVStrategy csvStrategy = new CSVStrategy(
				separator, quote, '#', true, true, true);
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		String[] line = parser.getLine();
		
		// read header
		if (line.length < 3)
			throw new DataFormatException("Almost 3 columns expected.");
		
		resultNames = new String[line.length - 2];
		System.arraycopy(line, 2, resultNames, 0, line.length - 2);
		//System.out.println("=-=-="+Arrays.toString(resultNames));
		
		// read body
		Map<String, Integer> condMap = new HashMap<String, Integer>();
		Map<String, Integer> groupMap = new HashMap<String, Integer>();
		List<Object[]> list = new ArrayList<Object[]>();
		
		while ((line = parser.getLine()) != null) {
			final String condName = line[0];
			final String groupName = line[1];
			
			Integer condIndex = condMap.get(condName);
			if (condIndex == null) {
				condIndex = condMap.size();
				condMap.put(condName, condIndex);
			}
			
			Integer groupIndex = groupMap.get(groupName);
			if (groupIndex == null) {
				groupIndex = groupMap.size();
				groupMap.put(groupName, groupIndex);
			}
			
			double[] data = new double[line.length - 2];
			
			for (int i = 2; i < line.length; i++) {
				final int dix = i - 2;
				try {
					data[dix] = Double.parseDouble(line[i]);
				}
				catch (NumberFormatException e) {
					data[dix] = Double.NaN;
				}
			}
			
			list.add(new Object[] {
					new int[] { groupIndex, condIndex }, data });
		}
		
		int numGroups = groupMap.size();
		int numConds = condMap.size();
	
		propNames = new String[numConds];
		condMap.keySet().toArray(propNames);
		
		groupNames = new String[numGroups];
		groupMap.keySet().toArray(groupNames);
		
		results = ObjectFactory2D.dense.make(numGroups, numConds);
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			double[] params = (double[]) result[1];
			
			results.set(coord[0], coord[1], params);
		}
	}

}
