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

package org.gitools.analysis.clustering.methods;

import java.io.IOException;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrixView;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractLoader;

// FIXME This code is very inefficient !!! check getNextInstance and getStructure

public class MatrixViewWekaLoader  extends AbstractLoader {

	private IMatrixView matrixView;

	private String indexValueMatrix;

	//FIXME very inefficient, use integer or enum instead,
	// or better use inheritance properly
	// or even better use MatrixViewTransposed
	private String type;

	private int indexRows;

	private int indexCols;


	public MatrixViewWekaLoader(IMatrixView matrixView, String index, String type) {

		this.matrixView = matrixView;

		this.indexValueMatrix = index;

		this.type = type;

		indexRows = indexCols = -1;

	}

	// FIXME structure should be constructed ONCE in the constructor
	// and returned here whenever it is needed
	@Override
	public Instances getStructure() throws IOException {

		FastVector attribNames = new FastVector();

		Integer capacity = 0;

		String name = "matrix"+type;

		if (type.equals("cols"))
		{
			//Adding attributes (rows name)
			for (int rows = 0; rows < matrixView.getRowCount(); rows++)

				attribNames.addElement(new Attribute(matrixView.getRowLabel(rows)));

		}
		else{
			//Adding attributes (columns name)

			for (int cols = 0; cols < matrixView.getColumnCount(); cols++)

				attribNames.addElement(new Attribute(matrixView.getColumnLabel(cols)));

		}

		return  new Instances(name, attribNames, capacity);


	}

	@Override
	public Instances getDataSet() throws IOException {

		Instances dataSet = getStructure();

		Instance current = null;

		Integer auxCols = indexCols, auxRows = indexRows;

		indexCols = -1;
		indexRows = -1;

		while ((current = getNextInstance(dataSet)) != null){
			dataSet.add(current);
		}
		indexCols = auxCols;
		indexRows = auxRows;
		return dataSet;

	}


	@Override
	//Param ds it is not modified nor altered
	public Instance getNextInstance(Instances ds) throws IOException
	{		

		Instance current = null;

		if (type.equals("cols"))
		{

			current = new Instance(matrixView.getRowCount());

			if ((matrixView.getVisibleColumns().length < 1) || (indexCols >= matrixView.getVisibleColumns().length-1)) return null;

			indexCols++;

			for (int row = 0;row < current.numAttributes();row++)
			{				
				current.setValue(row, MatrixUtils.doubleValue(
						matrixView.getCellValue(row,indexCols, indexValueMatrix)));
			}

		}
		else
		{
			current = new Instance(matrixView.getColumnCount());

			if ((matrixView.getVisibleRows().length < 1) || (indexRows >= matrixView.getVisibleRows().length-1)) return null;

			indexRows++;

			for (int col = 0;col < current.numAttributes();col++)
			{
					current.setValue(col, MatrixUtils.doubleValue(
						matrixView.getCellValue(indexRows, col, indexValueMatrix)));
			}
		}

		Instances dataset = getStructure();
		dataset.add(current);
		current.setDataset(dataset);
		
		return current;
	}

	@Override
	public String getRevision() {

		throw new UnsupportedOperationException("Not supported yet.");
		
	}
}
