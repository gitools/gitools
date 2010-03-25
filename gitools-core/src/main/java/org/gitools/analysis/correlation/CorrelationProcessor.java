/*
 *  Copyright 2010 cperez.
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

package org.gitools.analysis.correlation;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;


public class CorrelationProcessor {

	protected CorrelationAnalysis analysis;

	public CorrelationProcessor(CorrelationAnalysis analysis) {
		this.analysis = analysis;
	}

	public void run(IProgressMonitor monitor) {


		final IMatrix data = analysis.getData();
		int attributeIndex = 0;

		// TODO transposition if correlation for rows selected

		int numRows = data.getRowCount();
		int numColumns = data.getColumnCount();

		ObjectMatrix2D results = ObjectFactory2D.dense.make(numColumns, numColumns);
		
		String[] labels = new String[numColumns];
		for (int i = 0; i < numColumns; i++)
			labels[i] = data.getColumnLabel(i);
		
		DoubleMatrix2D buffer = DoubleFactory2D.dense.make(numRows, 2);
		//FIXME use double[]

		double[] x = new double[numRows];
		double[] y = new double[numRows];

		for (int i = 0; i < numColumns; i++) {
			for (int j = i + 1; j < numColumns; j++) {
				// TODO count how many pairs to compare
				/*int numPairs = 0;

				x = numPairs == x.length ? x : new double[numPairs];
				y = numPairs == y.length ? y : new double[numPairs];*/

				// Copy compared columns data to buffer
				for (int row = 0; row < numRows; row++) {
					double v0 = MatrixUtils.doubleValue(data.getCellValue(row, i, attributeIndex));
					x[row] = v0;
					
					double v1 = MatrixUtils.doubleValue(data.getCellValue(row, j, attributeIndex));
					y[row] = v1;
				}


			}
		}
	}
}
