package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Data;

public class DataResource extends Resource {

	private static final CSVStrategy csvStrategy = defaultCsvStrategy;
	
	public DataResource(String fileName) {
		super(fileName);
	}
	
	public DataResource(File file) {
		super(file);
	}
	
	public Data load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {

		Data data = new Data();
		
		loadMetadata(data, monitor);
		
		loadData(data, null, null, monitor);
		
		return data;
	}

	public void loadMetadata(Data data, ProgressMonitor monitor)
			throws FileNotFoundException, IOException, DataFormatException {
		
		monitor.begin("Reading names ...", 1);
		
		Reader reader = openReader();
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		// Read header
		
		final String[] header = parser.getLine();
		if (header.length < 2)
			throw new DataFormatException("At least 2 columns expected.");
		
		// Read datafile name and column names
		
		data.setName(header[0]);
		
		int numColumns = header.length - 1;
		
		String[] columnNames = new String[numColumns];
		System.arraycopy(header, 1, columnNames, 0, numColumns);
		data.setColNames(columnNames);
		
		// Read row names
		
		String[] fields;
		
		final List<String> names = new ArrayList<String>(); 
		while ((fields = parser.getLine()) != null)
			names.add(fields[0]);
		
		String[] rowNames = names.toArray(new String[names.size()]);
		data.setRowNames(rowNames);
		
		reader.close();
		
		monitor.info(columnNames.length + " columns");
		monitor.info(rowNames.length + " rows");
		
		monitor.end();
	}
	
	public void loadData( 
		Data data, 
		int[] columnsOrder,
		int[] rowsOrder, 
		ProgressMonitor monitor) throws FileNotFoundException, IOException {
		
		monitor.begin("Reading data ...", 1);
		
		int numColumns = data.getColNames().length;
		int numItems = data.getRowNames().length;
		
		String[] columnNames = data.getColNames();
		String[] rowNames = data.getRowNames();
		
		// Sort column names ordered by columnsOrder
		
		final String[] finalColumnNames = new String[numColumns];
		for (int i = 0; i < numColumns; i++) {
			int colidx = columnsOrder != null ? columnsOrder[i] : i;
			finalColumnNames[colidx] = columnNames[i];
		}
		columnNames = finalColumnNames;
		
		// Read rows names and data ordered by rowsOrder
		
		Reader reader = openReader();
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		parser.getLine(); // discard header
		
		DoubleMatrix2D matrix = 
			DoubleFactory2D.dense.make(numItems, numColumns);
		
		data.setData(matrix);
		
		String[] fields;
		int row = 0;
		
		while ((fields = parser.getLine()) != null) {
			
			int rowidx = rowsOrder != null ? rowsOrder[row] : row;
			
			if (rowidx >= 0) {
				rowNames[rowidx] = fields[0];
				
				int col = 0;
				
				while (col < numColumns && col < fields.length - 1) {
				
					int colidx = columnsOrder != null ? columnsOrder[col] : col;
					
					double value = Double.NaN;
					try {
						value = Double.parseDouble(fields[col + 1]);
					}
					catch (NumberFormatException e) {}
					
					matrix.setQuick(rowidx, colidx, value);
					col++;
				}
				
				// fill the rest of the columns with NaNs
				while (col < numColumns) {
					int colidx = columnsOrder != null ? columnsOrder[col] : col;
					matrix.setQuick(rowidx, colidx, Double.NaN);
					col++;
				}
			}
			row++;
		}
		
		reader.close();
		
		monitor.end();
	}
	
	public void save(Data data, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException {
		
		Writer writer = openWriter();
		
		PrintWriter pw = new PrintWriter(writer);
		
		DoubleMatrix2D matrix = data.getData();
		
		int numCols = matrix.columns();
		
		final String[] colNames = data.getColNames();
		
		pw.print(data.getName() != null ? data.getName() : "");
		
		for (int i = 0; i < numCols; i++) {
			final String name = i < colNames.length ? colNames[i] : "";
			pw.print('\t');
			pw.print(name);
		}
		
		pw.print('\n');
		
		int numRows = matrix.rows();
		
		final String[] rowNames = data.getRowNames();
		
		for (int i = 0; i < numRows; i++) {
			final String name = i < rowNames.length ? rowNames[i] : "";
			
			pw.print(name);
			
			for (int j = 0; j < numCols; j++) {
				double value = matrix.getQuick(i, j);
				pw.print('\t');
				if (Double.isNaN(value))
					pw.print('-');
				else
					pw.print(value);
			}
			
			pw.print('\n');
		}
		
		writer.close();
	}
}
