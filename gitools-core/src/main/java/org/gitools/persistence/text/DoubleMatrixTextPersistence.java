package org.gitools.persistence.text;

import java.io.File;

import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class DoubleMatrixTextPersistence 
	extends MatrixTextPersistence<DoubleMatrix> {

	private static final long serialVersionUID = 1995227069362928225L;

	public DoubleMatrixTextPersistence() {
	}

	@Override
	protected DoubleMatrix createEntity() {
		return new DoubleMatrix();
	}

	@Override
	public DoubleMatrix read(
			File file, 
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		return read(file, new DoubleTranslator(), monitor);
	}
	
	public void readData(
			File file,
			DoubleMatrix doubleMatrix,
			int[] columnsOrder, int[] rowsOrder,
			IProgressMonitor monitor)
			throws PersistenceException {

		readData(file, doubleMatrix, new DoubleTranslator(),
				columnsOrder, rowsOrder, monitor);
	}

	@Override
	public void write(
			File file,
			DoubleMatrix doubleMatrix,
			IProgressMonitor monitor) 
			throws PersistenceException {

		write(file, doubleMatrix, new DoubleTranslator(), monitor);
	}
}
