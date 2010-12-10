/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.text;

import java.io.File;

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
		/*return isBinaryValues() ?
			new DoubleBinaryMatrix() : new DoubleMatrix();*/

		return new DoubleMatrix();
	}

	@Override
	public DoubleMatrix read(
			File file, 
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		return read(file, getValueTranslator(), monitor);
	}
	
	public void readData(
			File file,
			DoubleMatrix doubleMatrix,
			int[] columnsOrder, int[] rowsOrder,
			IProgressMonitor monitor)
			throws PersistenceException {

		readData(file, doubleMatrix, getValueTranslator(),
				columnsOrder, rowsOrder, monitor);
	}

	@Override
	public void write(
			File file,
			DoubleMatrix doubleMatrix,
			IProgressMonitor monitor) 
			throws PersistenceException {

		write(file, doubleMatrix, getValueTranslator(), monitor);
	}
}
