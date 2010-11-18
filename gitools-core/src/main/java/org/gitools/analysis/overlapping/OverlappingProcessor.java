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

package org.gitools.analysis.overlapping;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Date;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;


public class OverlappingProcessor implements AnalysisProcessor {

	private OverlappingAnalysis analysis;

	public OverlappingProcessor(OverlappingAnalysis analysis) {
		this.analysis = analysis;
	}

	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {
		Date startTime = new Date();

		IMatrix data = analysis.getData();

		int attrIndex = -1;
		String attrName = analysis.getAttributeName();
		if (attrName != null && !attrName.isEmpty())
			attrIndex = data.getCellAttributeIndex(attrName);

		if (analysis.isTransposeData()) {
			TransposedMatrixView mt = new TransposedMatrixView();
			mt.setMatrix(data);
			data = mt;
		}

		int numRows = data.getRowCount();
		int numColumns = data.getColumnCount();

		monitor.begin("Running Overlapping analysis ...", numColumns * (numColumns - 1) / 2);

		String[] labels = new String[numColumns];
		for (int i = 0; i < numColumns; i++)
			labels[i] = data.getColumnLabel(i);

		final ObjectMatrix results = new ObjectMatrix();
		analysis.setResults(results);

		results.setColumns(labels);
		results.setRows(labels);
		results.makeCells();

		results.setCellAdapter(
				new BeanElementAdapter(OverlappingResult.class));

		double[] x = new double[numRows];
		double[] y = new double[numRows];
		int[] indices = new int[numRows];

		Double replaceNanValue = analysis.getReplaceNanValue();
		if (replaceNanValue == null)
			replaceNanValue = Double.NaN;

		Class<?> valueClass = data.getCellAttributes().get(attrIndex).getValueClass();
		final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);

		for (int i = 0; i < numColumns && !monitor.isCancelled(); i++) {
			for (int row = 0; row < numRows; row++) {
				Object value = data.getCellValue(row, i, attrIndex);
				Double v = cast.getDoubleValue(value);
				if (v == null || Double.isNaN(v))
					v = replaceNanValue;
				x[row] = v;
			}

			for (int j = i; j < numColumns && !monitor.isCancelled(); j++) {
				monitor.info("Overlapping " + data.getColumnLabel(i) + " with " + data.getColumnLabel(j));

				//TODO Parallelize
				{
					int numPairs = 0;
					for (int row = 0; row < numRows; row++) {
						double v0 = x[row];

						Object value = data.getCellValue(row, j, attrIndex);

						Double v1 = cast.getDoubleValue(value);
						if (v1 == null || Double.isNaN(v1))
							v1 = replaceNanValue;

						if (!Double.isNaN(v0) && !Double.isNaN(v1)) {
							y[row] = v1;

							indices[numPairs++] = row;
						}
					}

					/*try {
						results.setCell(i, j,
								method.Overlapping(x, y, indices, numPairs));
					} catch (MethodException ex) {
						throw new AnalysisException(ex);
					}*/
				}

				monitor.worked(1);
			}
		}

		analysis.setStartTime(startTime);
		analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

		monitor.end();
	}
}
