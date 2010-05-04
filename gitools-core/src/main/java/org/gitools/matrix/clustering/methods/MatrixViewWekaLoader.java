/*
 *  Copyright 2010 xrafael.
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
package org.gitools.matrix.clustering.methods;

import java.io.IOException;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrixView;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractLoader;

public class MatrixViewWekaLoader extends AbstractLoader {

	private IMatrixView matrixView;
	private Integer indexValueMatrix;
	private int indexRows;
	private int indexCols;
	private Instances structure;
	private FastVector attribNames;

	public MatrixViewWekaLoader(IMatrixView matrixView, Integer index) {

		this.matrixView = matrixView;

		this.indexValueMatrix = index;

		indexRows = indexCols = -1;

		attribNames = new FastVector();

		//Adding attributes (rows name)
		for (int rows = 0; rows < matrixView.getRowCount(); rows++)
			attribNames.addElement(new Attribute(matrixView.getRowLabel(rows)));

		structure = new Instances("matrixToCluster", attribNames, 0);

	}

	@Override
	public Instances getStructure() throws IOException {
		return structure;
	}

	@Override
	public Instances getDataSet() throws IOException {

		Instance current = null;

		Instances dataSet = new Instances("matrixToCluster", attribNames, 0);

		Integer auxCols = indexCols, auxRows = indexRows;

		indexCols = -1;
		indexRows = -1;

		while ((current = getNextInstance(dataSet)) != null)
			dataSet.add(current);

		indexCols = auxCols;
		indexRows = auxRows;

		return dataSet;
	}

	@Override
	//Param ds is not modified nor altered
	public Instance getNextInstance(Instances ds) throws IOException {

		if (indexCols >= matrixView.getColumnCount() - 1)
			return null;

		indexCols++;

		//MatrixViewInstance current = new MatrixViewInstance(matrixView,indexCols,indexValueMatrix);

		double[] values = new double[matrixView.getRowCount()];
		for (int row = 0; row < matrixView.getRowCount(); row++) {
			values[row] = MatrixUtils.doubleValue(
					matrixView.getCellValue(row, indexCols, indexValueMatrix));
		}

		//Instance is created once data in array values. This improves time performance
		Instance current = new Instance(1, values);

		Instances dataset = new Instances("matrixToCluster", attribNames, 0);
		dataset.add(current);
		current.setDataset(structure);

		return current;
	}

	@Override
	public String getRevision() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Given an index (col,row) from the matrix we retrieve the instance
	 *
	 */
	public Instance get(Integer index) {

		if (index >= matrixView.getVisibleColumns().length - 1)
			return null;

		double[] values = new double[matrixView.getRowCount()];

		for (int row = 0; row < matrixView.getRowCount(); row++) {
			values[row] = MatrixUtils.doubleValue(
					matrixView.getCellValue(row, index, indexValueMatrix));
		}

		//Instance is created once data in array values. This improves time performance
		Instance current = new Instance(1, values);

		//The dataset for the instance
		Instances dataset = new Instances("matrixToCluster", attribNames, 0);
		dataset.add(current);
		current.setDataset(dataset);

		return current;
	}
}
