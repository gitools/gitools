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

package org.gitools.matrix.clustering;

import java.io.IOException;
import java.util.Properties;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrixView;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class is an adapter for Weka Instances class
 * and ImatrixView class
 *
 */
public class MatrixViewWeka extends Instances{

	private IMatrixView matrixView;
	private Instances structure;
	private int[] indexes; //selected attributes from preprocessing
	private int dimMatrix;//the dimension of the matrix for obtaining the value
	private int initClassIndex;

	public MatrixViewWeka(Instances ds) {

		super(ds);
	}

	public void initialize(IMatrixView matrix, Properties params){//Integer dMatrix, Integer classIndex) {

		dimMatrix = new Integer (params.getProperty("index", "0")); 

		initClassIndex = m_ClassIndex = new Integer(params.getProperty("classIndex", "-1"));

		structure = new Instances("matrixToCluster", m_Attributes, 0);

		indexes = null;

		matrixView = matrix;

	}

	//Adding attributes (rows name)
	public FastVector addAttributes(int numAttributes) {

		FastVector attr = new FastVector();

		for (int rows = 0; rows < numAttributes; rows ++) {
			attr.addElement(new Attribute("a" + rows));
		}

		return attr;
	}

	public Instances getStructure() throws IOException {

		return structure;

	}

	public Instances getDataSet() throws IOException{

		Instance current = null;
		Instances dataSet = new Instances("matrixToCluster", m_Attributes, 0);
		try {
			for (int i = 0; i < matrixView.getVisibleColumns().length; i++) {
					current = get(i);
					dataSet.add(current);

			}
		} catch (Exception ex) {
			throw new IOException("Error retrieving Weka dataset");
		}
		return dataSet;

	}

	/**
	 * Given an index (col,row) from the matrix we retrieve the instance
	 *
	 */
	public Instance get(int index) throws Exception {

		if (index > matrixView.getVisibleColumns().length - 1) 	return null;

		double[] values = null;
		int row;
		final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
				matrixView.getCellAdapter().getProperty(dimMatrix).getValueClass());

		if (indexes == null)
		{
			values = new double[matrixView.getRowCount()];

			for (row = 0; row < matrixView.getRowCount(); row++) {
				try {
					values[row] = valueCast.getDoubleValue(
							matrixView.getCellValue(row, index, dimMatrix));
				} catch (Exception e) {
					values[row] = Double.NaN;
				}
			}
		}else
		{
			values = new double[indexes.length];

			for (int i = 0; i < indexes.length; i++) {
				try{
					row = indexes[i];
					values[i] = valueCast.getDoubleValue(
							matrixView.getCellValue(row, index, dimMatrix));
				} catch (Exception e) {
					values[i] = Double.NaN;
				}
			}
		}

		//Instance is created once data in array values. This improves time performance
		Instance current = new Instance(1, values);

		//The dataset for the instance
		Instances dataset = new Instances("matrixToCluster", m_Attributes, 0);
		dataset.setClassIndex(m_ClassIndex);

		current.setDataset(dataset);

		dataset.add(current);

		return current;
	}

	void setFilteredAttributes(int[] selectedAttributes) {

		indexes = selectedAttributes;
		m_Attributes = addAttributes(selectedAttributes.length);
		structure = new Instances("matrixToCluster", m_Attributes, 0);
		m_ClassIndex = initClassIndex;
	}

	void resetFilteredAttributes(){

		indexes = null;
		m_Attributes = addAttributes(matrixView.getRowCount());
		structure = new Instances("matrixToCluster", m_Attributes, 0);
	}

	@Override
	public int numInstances(){
		
		return matrixView.getVisibleColumns().length;
	}

	@Override
	public Instance instance(int i) {
		try {

			return get(i);

		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public int numAttributes() {

		if (indexes == null)
			return matrixView.getRowCount();
		else
			return indexes.length;

	}

	public Attribute attribute(Integer index) {

		return (Attribute) m_Attributes.elements(index);
	}

	@Override
	public int classIndex(){
		return m_ClassIndex;
	}

	public IMatrixView getMatrixView() {
		return matrixView;
	}


}
