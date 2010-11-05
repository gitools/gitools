package org.gitools.persistence.text;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.persistence.AbstractEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.utils.CSVStrategies;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class AnnotationMatrixTextPersistence 
		extends AbstractEntityPersistence<AnnotationMatrix> {
	
	@Override
	public AnnotationMatrix read(
			File file,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		AnnotationMatrix matrix = new AnnotationMatrix();
		
		try {
			Reader reader = PersistenceUtils.openReader(file);
			CSVParser parser = new CSVParser(reader, CSVStrategies.TSV);
			
			// header
			String[] hdr = parser.getLine();
			int numColumns = hdr.length - 1;
			ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
			for (int i = 0; i < numColumns; i++)
				columns.set(i, hdr[i + 1]);
			matrix.setColumns(columns);
			
			// body
			List<String> rawData = new ArrayList<String>();
			String[] fields;
			while ((fields = parser.getLine()) != null) {
				if (fields.length > hdr.length)
					throw new PersistenceException("Number of fields greater than number of header fields at line " + parser.getLineNumber());
				
				for (int i = 0; i < fields.length; i++)
					rawData.add(fields[i]);
				
				for (int i = 0; i < (hdr.length - fields.length); i++)
					rawData.add(new String(""));
			}
			
			int numRows = rawData.size() / hdr.length;
			ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
			ObjectMatrix2D data = ObjectFactory2D.dense.make(numRows, numColumns);
			int offs = 0;
			for (int row = 0; row < numRows; row++) {
				rows.setQuick(row, rawData.get(offs++));
				for (int col = 0; col < numColumns; col++)
					data.setQuick(row, col, rawData.get(offs++));
			}
			
			matrix.setRows(rows);
			matrix.setCells(data);
		
			rawData.clear();
			
			reader.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		
		return matrix;
	}
	
	@Override
	public void write(
			File file, 
			AnnotationMatrix entity,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// TODO Auto-generated method stub
		throw new PersistenceException("Unimplemented!!!");
	}
	
}
