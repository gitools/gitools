package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
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

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.ztools.test.results.AbstractResult;

public class ResultsFile extends ResourceFile {
	
	protected static final CSVStrategy csvStrategy = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
	private String[] condNames;
	private String[] groupNames;
	private String[] paramNames;
	private ObjectMatrix2D results;
	
	public ResultsFile() {
		super((String)null); //FIXME
	}
	
	public ResultsFile(String fileName) {
		super(fileName);
	}
	
	public ResultsFile(File file) {
		super(file);
	}
	
	public String[] getCondNames() {
		return condNames;
	}

	public void setCondNames(String[] condNames) {
		this.condNames = condNames;
	}

	public String[] getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String[] groupNames) {
		this.groupNames = groupNames;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public ObjectMatrix2D getResults() { 
		return results;
	}

	public void setResults(ObjectMatrix2D results) {
		this.results = results;
	}

	public void read() 
			throws FileNotFoundException, IOException, DataFormatException {
		
		read(openReader());
	}
	
	public void read(Reader reader) throws IOException, DataFormatException {
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		String[] line = parser.getLine();
		
		// read header
		if (line.length < 3)
			throw new DataFormatException("Almost 3 columns expected.");
		
		paramNames = new String[line.length - 2];
		System.arraycopy(line, 2, paramNames, 0, line.length - 2);
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
			
			//double[] data = new double[line.length - 2];
			DoubleMatrix1D data = DoubleFactory1D.dense.make(line.length - 2);
			
			for (int i = 2; i < line.length; i++) {
				final int dix = i - 2;
				try {
					//data[dix] = Double.parseDouble(line[i]);
					data.setQuick(dix, Double.parseDouble(line[i]));
				}
				catch (NumberFormatException e) {
					//data[dix] = Double.NaN;
					data.setQuick(dix, Double.NaN);
				}
			}
			
			list.add(new Object[] {
					new int[] { groupIndex, condIndex }, data });
		}
		
		int numGroups = groupMap.size();
		int numConds = condMap.size();
	
		condNames = new String[numConds];
		condMap.keySet().toArray(condNames);
		
		groupNames = new String[numGroups];
		groupMap.keySet().toArray(groupNames);
		
		results = ObjectFactory2D.dense.make(numGroups, numConds);
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			//double[] params = (double[]) result[1];
			DoubleMatrix1D params = (DoubleMatrix1D) result[1];
			
			results.set(coord[0], coord[1], params);
		}
	}

	/*public void write(Writer writer) {
		write(writer, true);
	}*/
	
	public void write(boolean orderByCondition) 
			throws FileNotFoundException, IOException {
		
		write(openWriter(), orderByCondition);
	}
	
	public void write(Writer writer, boolean orderByCondition) {
		RawCsvWriter out = new RawCsvWriter(writer, 
				csvStrategy.getDelimiter(), csvStrategy.getCommentStart());
		
		out.writeQuotedValue("condition");
		out.writeSeparator();
		out.writeQuotedValue("module");
		for (String resultName : paramNames) {
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
		out.writeQuotedValue(condNames[propIndex]);
		out.writeSeparator();
		out.writeQuotedValue(groupNames[groupIndex]);
		
		AbstractResult cell = 
			(AbstractResult) results.getQuick(groupIndex, propIndex);
		
		Object[] values = cell.getValues();
		
		for (int paramIndex = 0; paramIndex < paramNames.length; paramIndex++) {
			out.writeSeparator();
			out.writeValue(String.valueOf(values[paramIndex]));
		}
		
		out.writeNewLine();
	}

}
