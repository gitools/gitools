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

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;

import es.imim.bg.csv.RawCsvWriter;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.model.elements.ArrayElementFacade;
import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.model.elements.ElementProperty;
import es.imim.bg.ztools.model.elements.StringElementFacade;

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
		
		ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
		for (Entry<String, Integer> entry : columnMap.entrySet())
			columns.setQuick(entry.getValue(), entry.getKey());
		
		ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
		for (Entry<String, Integer> entry : rowMap.entrySet())
			rows.setQuick(entry.getValue(), entry.getKey());
		
		resultsMatrix.setColumns(columns);
		resultsMatrix.setRows(rows);
		resultsMatrix.makeData();
		
		resultsMatrix.setColumnsFacade(new StringElementFacade());
		resultsMatrix.setRowsFacade(new StringElementFacade());
		resultsMatrix.setCellsFacade(new ArrayElementFacade(paramNames));
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			final int columnIndex = coord[0];
			final int rowIndex = coord[1];
			
			double[] paramValues = (double[]) result[1];
			//DoubleMatrix1D params = (DoubleMatrix1D) result[1];
			
			resultsMatrix.setCell(rowIndex, columnIndex, paramValues);
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
		
		for (ElementProperty prop : resultsMatrix.getCellsFacade().getProperties()) {
			out.writeSeparator();
			out.writeQuotedValue(prop.getId());
		}
		
		out.writeNewLine();

		int numColumns = resultsMatrix.getColumnCount();
		int numRows = resultsMatrix.getRowCount();
		
		if (orderByColumn) {
			for (int colIndex = 0; colIndex < numColumns; colIndex++)
				for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex);
		}
		else {
			for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
				for (int colIndex = 0; colIndex < numColumns; colIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex);
		}
		
		out.close();
	}

	private void writeLine(
			RawCsvWriter out, 
			ResultsMatrix resultsMatrix,
			int colIndex, int rowIndex) {
		
		final String colName = resultsMatrix.getColumn(colIndex).toString();
		final String rowName = resultsMatrix.getRow(rowIndex).toString();
		
		out.writeQuotedValue(colName);
		out.writeSeparator();
		out.writeQuotedValue(rowName);
		
		Object element = resultsMatrix.getCell(rowIndex, colIndex);
		
		ElementFacade cellsFacade = resultsMatrix.getCellsFacade();
		
		int numProperties = cellsFacade.getPropertyCount();
		
		for (int propIndex = 0; propIndex < numProperties; propIndex++) {
			out.writeSeparator();
			
			Object value = cellsFacade.getValue(element, propIndex);
			if (value instanceof Double && Double.isNaN((Double) value))
				out.writeValue("-");
			else
				out.writeQuotedValue(value.toString());
		}
		
		out.writeNewLine();
	}
}