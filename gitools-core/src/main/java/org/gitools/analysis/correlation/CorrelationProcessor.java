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

package org.gitools.analysis.correlation;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.util.Date;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.methods.CorrelationMethodFactory;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;

public class CorrelationProcessor implements AnalysisProcessor {

	protected CorrelationAnalysis analysis;

	public CorrelationProcessor(CorrelationAnalysis analysis) {
		this.analysis = analysis;
	}

	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		Date startTime = new Date();

		CorrelationMethod method = CorrelationMethodFactory.createMethod(
				analysis.getMethod(), analysis.getMethodProperties());

		IMatrix data = analysis.getData();
		int attributeIndex = analysis.getAttributeIndex();

		if (analysis.isTransposeData()) {
			TransposedMatrixView mt = new TransposedMatrixView();
			mt.setMatrix(data);
			data = mt;
		}

		int numRows = data.getRowCount();
		int numColumns = data.getColumnCount();

		monitor.begin("Running correlation analysis ...", numColumns * (numColumns - 1) / 2);

		String[] labels = new String[numColumns];
		for (int i = 0; i < numColumns; i++)
			labels[i] = data.getColumnLabel(i);

		final ObjectMatrix results = new ObjectMatrix();
		analysis.setResults(results);

		results.setColumns(labels);
		results.setRows(labels);
		results.makeCells();

		results.setCellAdapter(
				new BeanElementAdapter(method.getResultClass()));
		
		double[] x = new double[numRows];
		double[] y = new double[numRows];
		int[] indices = new int[numRows];

		//boolean replaceNanValues = analysis.isReplaceNanValues();
		Double replaceNanValue = analysis.getReplaceNanValue();
		if (replaceNanValue == null)
			replaceNanValue = Double.NaN;

		Class<?> valueClass = data.getCellAttributes().get(attributeIndex).getValueClass();
		final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);

		for (int i = 0; i < numColumns && !monitor.isCancelled(); i++) {
			for (int row = 0; row < numRows; row++) {
				Object value = data.getCellValue(row, i, attributeIndex);
				Double v = cast.getDoubleValue(value);
				if (v == null || Double.isNaN(v))
					v = replaceNanValue;
				x[row] = v;
			}

			for (int j = i; j < numColumns && !monitor.isCancelled(); j++) {
				monitor.info("Correlating " + data.getColumnLabel(i) + " with " + data.getColumnLabel(j));

				//TODO Parallelize
				{
					int numPairs = 0;
					for (int row = 0; row < numRows; row++) {
						double v0 = x[row];

						Object value = data.getCellValue(row, j, attributeIndex);

						Double v1 = cast.getDoubleValue(value);
						if (v1 == null || Double.isNaN(v1))
							v1 = replaceNanValue;

						if (!Double.isNaN(v0) && !Double.isNaN(v1)) {
							y[row] = v1;

							indices[numPairs++] = row;
						}
					}

					try {
						results.setCell(i, j,
								method.correlation(x, y, indices, numPairs));
					} catch (MethodException ex) {
						throw new AnalysisException(ex);
					}
				}

				monitor.worked(1);
			}
		}

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
		
		monitor.end();
	}
}
