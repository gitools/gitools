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
import java.util.Map.Entry;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.ResultsMatrix;

public class ResultsResource extends Resource {
	
	private static final CSVStrategy csvStrategy = defaultCsvStrategy;

	public ResultsResource() {
		super((String)null); //FIXME
	}
	
	public ResultsResource(String fileName) {
		super(fileName);
	}
	
	public ResultsResource(File file) {
		super(file);
	}

	public ResultsMatrix read(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		ResultsMatrix resultsMatrix = new ResultsMatrix();
		read(resultsMatrix, monitor);
		return resultsMatrix;
	}
	
	public void read(ResultsMatrix resultsMatrix, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		read(openReader(), resultsMatrix, monitor);
	}
	
	protected void read(Reader reader, ResultsMatrix resultsMatrix, ProgressMonitor monitor) 
			throws IOException, DataFormatException {
		
		monitor.begin("Reading results ...", 1);
		
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
		for (Entry<String, Integer> entry : columnMap.entrySet())
			columnNames[entry.getValue()] = entry.getKey();
		
		String[] rowNames = new String[numRows];
		for (Entry<String, Integer> entry : rowMap.entrySet())
			rowNames[entry.getValue()] = entry.getKey();
		
		resultsMatrix.setColNames(columnNames);
		resultsMatrix.setRowNames(rowNames);
		resultsMatrix.setParamNames(paramNames);
		resultsMatrix.makeData();
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			final int columnIndex = coord[0];
			final int rowIndex = coord[1];
			
			double[] paramValues = (double[]) result[1];
			//DoubleMatrix1D params = (DoubleMatrix1D) result[1];
			
			for (int pi = 0; pi < numParams; pi++)
				resultsMatrix.setDataValue(columnIndex, rowIndex, pi, paramValues[pi]);
		}
		
		monitor.end();
	}
	
	public void write(ResultsMatrix resultsMatrix, boolean orderByColumn, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException {
		
		write(openWriter(), resultsMatrix, orderByColumn, monitor);
	}
	
	public void write(Writer writer, ResultsMatrix resultsMatrix, boolean orderByColumn, ProgressMonitor monitor) {
		
		RawCsvWriter out = new RawCsvWriter(writer, 
				csvStrategy.getDelimiter(), csvStrategy.getEncapsulator());
		
		out.writeQuotedValue("column");
		out.writeSeparator();
		out.writeQuotedValue("row");
		
		for (String resultName : resultsMatrix.getParamNames()) {
			out.writeSeparator();
			out.writeQuotedValue(resultName);
		}
		
		out.writeNewLine();
		
		final String[] columnNames = resultsMatrix.getColNames();
		final String[] rowNames = resultsMatrix.getRowNames();
		final String[] paramNames = resultsMatrix.getParamNames();
		
		int numColumns = resultsMatrix.getData().columns();
		int numRows = resultsMatrix.getData().rows();
		
		if (orderByColumn) {
			for (int colIndex = 0; colIndex < numColumns; colIndex++)
				for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
					writeLine(out, resultsMatrix, columnNames[colIndex], rowNames[rowIndex], 
							paramNames, colIndex, rowIndex);
		}
		else {
			for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
				for (int colIndex = 0; colIndex < numColumns; colIndex++)
					writeLine(out, resultsMatrix, columnNames[colIndex], rowNames[rowIndex], 
							paramNames, colIndex, rowIndex);
		}
		
		out.close();
	}

	private void writeLine(RawCsvWriter out, ResultsMatrix resultsMatrix, 
			String colName, String rowName, String[] paramNames, int colIndex, int rowIndex) {
		
		out.writeQuotedValue(colName);
		out.writeSeparator();
		out.writeQuotedValue(rowName);
		
		for (int paramIndex = 0; paramIndex < paramNames.length; paramIndex++) {
			out.writeSeparator();
			out.writeValue(String.valueOf(
					resultsMatrix.getDataValue(colIndex, rowIndex, paramIndex)));
		}
		
		out.writeNewLine();
	}
}
