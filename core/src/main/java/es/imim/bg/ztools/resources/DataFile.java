package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;

public class DataFile extends ResourceFile {

	protected static final CSVStrategy csvStrategy = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
	// Output
	
	protected String name;
	
	protected String[] columnNames;
	protected String[] rowNames;
	
	protected DoubleMatrix2D data;
	
	public DataFile(String fileName) {
		super(fileName);
	}
	
	public DataFile(File file) {
		super(file);
	}
	
	public void load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {

		loadMetadata(monitor);
		
		loadData(null, null, monitor);
	}

	public void loadMetadata(ProgressMonitor monitor)
			throws FileNotFoundException, IOException, DataFormatException {
		
		monitor.begin("Reading names ...", 1);
		
		Reader reader = openReader();
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		// Read header
		
		final String[] header = parser.getLine();
		if (header.length < 2)
			throw new DataFormatException("At least 2 columns expected.");
		
		// Read datafile name and column names
		
		name = header[0];
		
		int numColumns = header.length - 1;
		
		columnNames = new String[numColumns];
		System.arraycopy(header, 1, columnNames, 0, numColumns);
		
		// Read row names
		
		String[] fields;
		
		final List<String> names = new ArrayList<String>(); 
		while ((fields = parser.getLine()) != null)
			names.add(fields[0]);
		
		rowNames = names.toArray(new String[names.size()]);
		
		reader.close();
		
		monitor.info(columnNames.length + " columns");
		monitor.info(rowNames.length + " rows");
		
		monitor.end();
	}
	
	public void loadData( 
		int[] columnsOrder,
		int[] rowsOrder, 
		ProgressMonitor monitor) throws FileNotFoundException, IOException {
		
		monitor.begin("Reading data ...", 1);
		
		int numColumns = columnNames.length;
		int numItems = rowNames.length;
		
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
		
		data = DoubleFactory2D.dense.make(numItems, numColumns);
		
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
					
					data.setQuick(rowidx, colidx, value);
					col++;
				}
				
				// fill the rest of the columns with NaNs
				while (col < numColumns) {
					int colidx = columnsOrder != null ? columnsOrder[col] : col;
					data.setQuick(rowidx, colidx, Double.NaN);
					col++;
				}
			}
			row++;
		}
		
		reader.close();
		
		monitor.end();
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public String[] getRowNames() {
		return rowNames;
	}
	
	public DoubleMatrix2D getData() {
		return data;
	}
}
