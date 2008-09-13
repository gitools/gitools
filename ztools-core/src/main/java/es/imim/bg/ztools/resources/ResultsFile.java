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

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.ztools.model.Results;

public class ResultsFile extends ResourceFile {
	
	protected static final CSVStrategy csvStrategy = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
	/*private String[] columnNames;
	private String[] rowNames;
	private String[] paramNames;
	
	//private ObjectMatrix2D data;
	private DoubleMatrix3D data;
	*/
	public ResultsFile() {
		super((String)null); //FIXME
	}
	
	public ResultsFile(String fileName) {
		super(fileName);
	}
	
	public ResultsFile(File file) {
		super(file);
	}

	public Results read() 
			throws FileNotFoundException, IOException, DataFormatException {
		
		Results results = new Results();
		read(results);
		return results;
	}
	
	public void read(Results results) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		read(openReader(), results);
	}
	
	protected void read(Reader reader, Results results) throws IOException, DataFormatException {
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		String[] line = parser.getLine();
		
		// read header
		if (line.length < 3)
			throw new DataFormatException("Almost 3 columns expected.");
		
		int numParams = line.length - 2;
		String[] paramNames = new String[numParams];
		System.arraycopy(line, 2, paramNames, 0, line.length - 2);
		
		// read body
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		Map<String, Integer> rowMap = new HashMap<String, Integer>();
		List<Object[]> list = new ArrayList<Object[]>();
		
		while ((line = parser.getLine()) != null) {
			final String columnName = line[0];
			final String rowName = line[1];
			
			Integer columnIndex = columnMap.get(columnName);
			if (columnIndex == null) {
				columnIndex = columnMap.size();
				columnMap.put(columnName, columnIndex);
			}
			
			Integer rowIndex = rowMap.get(rowName);
			if (rowIndex == null) {
				rowIndex = rowMap.size();
				rowMap.put(rowName, rowIndex);
			}
			
			double[] data = new double[line.length - 2];
			//DoubleMatrix1D data = DoubleFactory1D.dense.make(line.length - 2);
			
			for (int i = 2; i < line.length; i++) {
				final int dix = i - 2;
				try {
					data[dix] = Double.parseDouble(line[i]);
					//data.setQuick(dix, Double.parseDouble(line[i]));
				}
				catch (NumberFormatException e) {
					data[dix] = Double.NaN;
					//data.setQuick(dix, Double.NaN);
				}
			}
			
			list.add(new Object[] {
					new int[] { columnIndex, rowIndex }, data });
		}
		
		int numColumns = columnMap.size();
		int numRows = rowMap.size();
		
		String[] columnNames = new String[numColumns];
		columnMap.keySet().toArray(columnNames);
		
		String[] rowNames = new String[numRows];
		rowMap.keySet().toArray(rowNames);
		
		DoubleMatrix3D data = 
			DoubleFactory3D.dense.make(numRows, numColumns, numParams);
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			final int columnIndex = coord[0];
			final int rowIndex = coord[1];
			
			double[] paramValues = (double[]) result[1];
			//DoubleMatrix1D params = (DoubleMatrix1D) result[1];
			
			for (int pi = 0; pi < numParams; pi++)
				data.setQuick(columnIndex, rowIndex, pi, paramValues[pi]);
		}
		
		results.setColNames(columnNames);
		results.setRowNames(rowNames);
		results.setParamNames(paramNames);
		results.setData(data);
	}
	
	public void write(Results results, boolean orderByColumn) 
			throws FileNotFoundException, IOException {
		
		write(openWriter(), results, orderByColumn);
	}
	
	public void write(Writer writer, Results results, boolean orderByColumn) {
		
		RawCsvWriter out = new RawCsvWriter(writer, 
				csvStrategy.getDelimiter(), csvStrategy.getCommentStart());
		
		out.writeQuotedValue("column");
		out.writeSeparator();
		out.writeQuotedValue("row");
		
		for (String resultName : results.getParamNames()) {
			out.writeSeparator();
			out.writeQuotedValue(resultName);
		}
		
		out.writeNewLine();
		
		final String[] columnNames = results.getColNames();
		final String[] rowNames = results.getRowNames();
		final String[] paramNames = results.getParamNames();
		
		int numColumns = results.getData().columns();
		int numRows = results.getData().rows();
		
		if (orderByColumn) {
			for (int colIndex = 0; colIndex < numColumns; colIndex++)
				for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
					writeLine(out, results, columnNames[colIndex], rowNames[rowIndex], 
							paramNames, colIndex, rowIndex);
		}
		else {
			for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
				for (int colIndex = 0; colIndex < numColumns; colIndex++)
					writeLine(out, results, columnNames[colIndex], rowNames[rowIndex], 
							paramNames, colIndex, rowIndex);
		}
		
		out.close();
	}

	private void writeLine(RawCsvWriter out, Results results, 
			String colName, String rowName, String[] paramNames, int colIndex, int rowIndex) {
		
		out.writeQuotedValue(colName);
		out.writeSeparator();
		out.writeQuotedValue(rowName);
		
		for (int paramIndex = 0; paramIndex < paramNames.length; paramIndex++) {
			out.writeSeparator();
			out.writeValue(String.valueOf(
					results.getDataValue(colIndex, rowIndex, paramIndex)));
		}
		
		out.writeNewLine();
	}

}
