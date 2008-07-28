package es.imim.bg.ztools.loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.csv.CSVException;
import es.imim.bg.csv.CSVProcessor;
import es.imim.bg.csv.CSVProcessorAdapter;
import es.imim.bg.csv.CSVReader;
import es.imim.bg.ztools.model.DataMatrix;

public class DataFileLoader implements DataLoader {
	
	private static final char defaultSeparator = '\t';
	private static final char defaultQuote = '"';
	
	private File file;
	
	private String dataName;
	private List<String> colNames;
	private List<String> rowNames;
	private DoubleMatrix2D data;
	
	public DataFileLoader(File file) {
		this.file = file;
	}
	
	public DataMatrix load() throws IOException, CSVException {
		dataName = "";
		colNames = new ArrayList<String>();
		rowNames = new ArrayList<String>();
		
		/*FileInputStream in;
		String name = file.getName();
		int i = name.lastIndexOf('.');
		if (i >= 0 && name.substring(i).equals("gz"))
			in = new GZIPInputStream(in);*/
		CSVReader csv = new CSVReader(file, defaultSeparator, defaultQuote);
		
		// Load column names
		loadHeader(csv);
		
		// Count number of rows and check number of columns consistency
		prepareData(csv);
		
		// Restart and skip header
		csv.reset();
		csv.scanLine(null);
		
		// Load data
		loadData(csv);
		
		return new DataMatrix(dataName, colNames, rowNames, data);
	}

	private void loadHeader(CSVReader csv) throws IOException, CSVException {
		csv.scanLine(new CSVProcessorAdapter() {
			@Override
			public boolean field(String field, int row, int col) {
				if (col > 0)
					colNames.add(field);
				else
					dataName = field;
				return true;
			}
		});
	}
	
	private void prepareData(CSVReader csv) throws CSVException {
		final int numCols = colNames.size();
		
		CSVProcessor proc = new CSVProcessorAdapter() {
			int numFields;
			boolean emptyLine;
			
			@Override
			public boolean lineStart(int row) {
				numFields = 0;
				emptyLine = false;
				return true;
			}
			
			@Override
			public boolean field(String field, int row, int col)  {
				
				if (col == 0) {
					if (field.length() == 0)
						emptyLine = true;
					else
						rowNames.add(field);
				}
				else
					numFields++;
				
				return true;
			}
			
			@Override
			public boolean lineEnd(int row) throws CSVException {
				if (emptyLine) {
					if (numFields > 0)
						throw new CSVException(row, numFields, "Row without name.");
				}
				else {
					if (numFields < numCols)
						throw new CSVException(row, numFields, "Less columns than expected.");
					
					if (numFields > numCols)
						throw new CSVException(row, numFields, "More columns than expected.");
				}
				
				return true;
			}
		};
		
		csv.scan(proc);
	
		int numRows = rowNames.size();
		
		data = DoubleFactory2D.dense.make(numRows, numCols);
	}
	
	private void loadData(CSVReader csv) throws IOException, CSVException {
		CSVProcessor proc = new CSVProcessorAdapter() {
			
			@Override
			public boolean field(String field, int row, int col) throws CSVException  {
				
				if (col != 0) {
					double value;
					try {
						value = Double.parseDouble(field);
					} catch (NumberFormatException e) {
						//throw new CSVException(row, col, "Invalid data.", e);
						value = Double.NaN;
					}
					data.setQuick(row - 1, col - 1, value);
				}
				
				return true;
			}
		};
		
		csv.scan(proc);
	}
}
