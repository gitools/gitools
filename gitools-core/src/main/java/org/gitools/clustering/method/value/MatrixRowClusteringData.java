/*
 *  Copyright 2011 chris.
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

package org.gitools.clustering.method.value;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringDataInstance;
import org.gitools.matrix.model.IMatrix;

public class MatrixRowClusteringData implements ClusteringData {

	public class Instance implements ClusteringDataInstance {

		protected int index;

		public Instance(int index) {
			this.index = index;
		}

		@Override
		public int getNumAttributes() {
			return matrix.getColumnCount();
		}

		@Override
		public String getAttributeName(int attribute) {
			return matrix.getColumnLabel(attribute);
		}

		@Override
		public Class<?> getValueClass(int attribute) {
			return attributeClass;
		}

		@Override
		public Object getValue(int attribute) {
			return matrix.getCellValue(index, attribute, matrixAttribute);
		}

		@Override
		public <T> T getTypedValue(int attribute, Class<T> valueClass) {
			if (!valueClass.equals(getValueClass(attribute)))
				throw new RuntimeException("Unsupported type: " + valueClass.getCanonicalName());

			return (T) matrix.getCellValue(index, attribute, matrixAttribute);
		}
	}

	private IMatrix matrix;
	private int matrixAttribute;
	private Class<?> attributeClass;

	public MatrixRowClusteringData(IMatrix matrix, int matrixAttribute) {
		this.matrix = matrix;
		this.matrixAttribute = matrixAttribute;
		this.attributeClass = matrix.getCellAttributes().get(matrixAttribute).getValueClass();
	}

	@Override
	public int getSize() {
		return matrix.getRowCount();
	}

	@Override
	public String getLabel(int index) {
		return matrix.getRowLabel(index);
	}

	@Override
	public ClusteringDataInstance getInstance(int index) {
		return new Instance(index);
	}

	public Class<?> getAttributeClass() {
		return attributeClass;
	}

	public void setAttributeClass(Class<?> attributeClass) {
		this.attributeClass = attributeClass;
	}

	public IMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(IMatrix matrix) {
		this.matrix = matrix;
	}

	public int getNumAttributes() {
		return matrix.getColumnCount();
	}
}
