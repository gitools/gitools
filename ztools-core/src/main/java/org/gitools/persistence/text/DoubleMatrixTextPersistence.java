package org.gitools.persistence.text;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.gitools.datafilters.DoubleParser;
import org.gitools.datafilters.ValueParser;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.AbstractEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.utils.CSVStrategies;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class DoubleMatrixTextPersistence 
	extends AbstractEntityPersistence<DoubleMatrix> {

	private static final long serialVersionUID = 1995227069362928225L;

	public DoubleMatrixTextPersistence() {
	}

	@Override
	public DoubleMatrix read(
			File file, 
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		return read(file, new DoubleParser(), monitor);
	}
	
	public DoubleMatrix read(
			File file, 
			ValueParser filt, 
			IProgressMonitor monitor) 
			throws PersistenceException {

		DoubleMatrix doubleMatrix = new DoubleMatrix();
		
		readMetadata(file, doubleMatrix, filt, monitor);
		
		readData(file, doubleMatrix, filt, null, null, monitor);
		
		return doubleMatrix;
	}

	public void readMetadata(
			File file,
			DoubleMatrix doubleMatrix, 
			IProgressMonitor monitor)
			throws PersistenceException {
		
		readMetadata(file, doubleMatrix, new DoubleParser(), monitor);
	}
	
	public void readMetadata(
			File file, 
			DoubleMatrix doubleMatrix, 
			ValueParser filt, 
			IProgressMonitor monitor)
			throws PersistenceException {
		
		monitor.begin("Reading names ...", 1);
		
		Reader reader = null;
		try {
			reader = PersistenceUtils.openReader(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}
		
		CSVParser parser = new CSVParser(reader, CSVStrategies.TSV);
		
		// Read header

		try {
			final String[] header = parser.getLine();
			if (header.length < 2)
				throw new DataFormatException("At least 2 columns expected.");
			
			// Read datafile name and column names
			
			doubleMatrix.setName(header[0]);
			
			int numColumns = header.length - 1;
			
			String[] columnNames = new String[numColumns];
			System.arraycopy(header, 1, columnNames, 0, numColumns);
			doubleMatrix.setColumns(columnNames);
			
			// Read row names
			
			String[] fields;
			
			final List<String> names = new ArrayList<String>(); 
			while ((fields = parser.getLine()) != null)
				names.add(fields[0]);
			
			String[] rowNames = names.toArray(new String[names.size()]);
			doubleMatrix.setRows(rowNames);
			
			reader.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		monitor.info(doubleMatrix.getColumnCount() + " columns");
		monitor.info(doubleMatrix.getRowCount() + " rows");
		
		monitor.end();
	}
	
	public void readData(
			File file,
			DoubleMatrix doubleMatrix, int[] columnsOrder,
			int[] rowsOrder, IProgressMonitor monitor) 
			throws PersistenceException {

		readData(file, doubleMatrix, new DoubleParser(), columnsOrder, rowsOrder, monitor);
	}
	
	public void readData(
			File file,
			DoubleMatrix doubleMatrix, 
			ValueParser filt,
			int[] columnsOrder, 
			int[] rowsOrder, 
			IProgressMonitor monitor)
			throws PersistenceException {
		
		monitor.begin("Reading data ...", 1);
		
		int numColumns = doubleMatrix.getColumnCount();
		int numItems = doubleMatrix.getRowCount();
		
		String[] columnNames = doubleMatrix.getColumnStrings();
		String[] rowNames = doubleMatrix.getRowStrings();
		
		// Sort column names ordered by columnsOrder
		
		final String[] finalColumnNames = new String[numColumns];
		for (int i = 0; i < numColumns; i++) {
			int colidx = columnsOrder != null ? columnsOrder[i] : i;
			finalColumnNames[colidx] = columnNames[i];
		}
		columnNames = finalColumnNames;
		
		// Read rows names and data ordered by rowsOrder
		
		Reader reader;
		try {
			reader = PersistenceUtils.openReader(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}
		
		try {
			CSVParser parser = new CSVParser(reader, CSVStrategies.TSV);
			
			parser.getLine(); // discard header
			
			DoubleMatrix2D matrix = 
				DoubleFactory2D.dense.make(numItems, numColumns);
			
			doubleMatrix.setData(matrix);
			
			String[] fields;
			int row = 0;
			
			while ((fields = parser.getLine()) != null) {
				
				int rowidx = rowsOrder != null ? rowsOrder[row] : row;
				
				if (rowidx >= 0) {
					rowNames[rowidx] = fields[0];
					
					int col = 0;
					
					while (col < numColumns && col < fields.length - 1) {
					
						int colidx = columnsOrder != null ? columnsOrder[col] : col;
						
						double value = filt.parseValue(fields[col + 1]);
						
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
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		monitor.end();
	}
	
	@Override
	public void write(
			File file,
			DoubleMatrix doubleMatrix,
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		Writer writer;
		try {
			writer = PersistenceUtils.openWriter(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}
		
		PrintWriter pw = new PrintWriter(writer);
		
		DoubleMatrix2D matrix = doubleMatrix.getData();
		
		int numCols = matrix.columns();
		
		final String[] colNames = doubleMatrix.getColumnStrings();
		
		pw.print(doubleMatrix.getName() != null ? doubleMatrix.getName() : "");
		
		for (int i = 0; i < numCols; i++) {
			final String name = i < colNames.length ? colNames[i] : "";
			pw.print('\t');
			pw.print(name);
		}
		
		pw.print('\n');
		
		int numRows = matrix.rows();
		
		final String[] rowNames = doubleMatrix.getRowStrings();
		
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
		
		try {
			writer.close();
		} catch (Exception e) {
			throw new PersistenceException("Error closing resource: " + file.getName(), e);
		}
	}
}
