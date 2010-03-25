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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Properties;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.methods.PearsonCorrelationMethod;
import org.gitools.analysis.correlation.methods.SpearmanCorrelationMethod;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.MatrixViewTransposition;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;

public class CorrelationProcessor {

	protected CorrelationAnalysis analysis;

	public CorrelationProcessor(CorrelationAnalysis analysis) {
		this.analysis = analysis;
	}

	public void run(IProgressMonitor monitor) throws AnalysisException {

		CorrelationMethod method = createMethod(
				analysis.getMethodId(), analysis.getMethodProperties());

		IMatrix data = analysis.getData();
		int attributeIndex = analysis.getAttributeIndex();

		if (analysis.isTransposeData()) {
			MatrixViewTransposition mt = new MatrixViewTransposition();
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

		boolean replaceNanValues = analysis.isReplaceNanValues();
		double nanValue = analysis.getNanValue();

		for (int i = 0; i < numColumns; i++) {
			for (int j = i + 1; j < numColumns; j++) {
				monitor.info("Correlating " + data.getColumnLabel(i) + " with " + data.getColumnLabel(j));
				
				int numPairs = gatherValues(
						data, numRows, i, j, x, y,
						attributeIndex, replaceNanValues, nanValue);

				double[] x2 = x;
				double[] y2 = y;

				if (numPairs != numRows) {
					x2 = new double[numPairs];
					y2 = new double[numPairs];

					System.arraycopy(x, 0, x2, 0, numPairs);
					System.arraycopy(y, 0, y2, 0, numPairs);
				}
				
				calculateCorr(method, x2, y2, results, i, j);

				monitor.worked(1);

				if (monitor.isCancelled())
					return;
			}
		}

		monitor.end();
	}

	private void calculateCorr(CorrelationMethod method, double[] x2, double[] y2, final ObjectMatrix results, int i, int j) throws AnalysisException {
		try {
			CorrelationResult result = method.correlation(x2, y2);
			results.setCell(i, j, result);
		} catch (MethodException ex) {
			throw new AnalysisException(ex);
		}
	}

	private int gatherValues(IMatrix data, int numRows, int i, int j, double[] x, double[] y, int attributeIndex, boolean replaceNanValues, double nanValue) {
		int numPairs = 0;
		for (int row = 0; row < numRows; row++) {
			double v0 = MatrixUtils.doubleValue(data.getCellValue(row, i, attributeIndex));
			if (replaceNanValues && Double.isNaN(v0)) {
				v0 = nanValue;
			}
			double v1 = MatrixUtils.doubleValue(data.getCellValue(row, j, attributeIndex));
			if (replaceNanValues && Double.isNaN(v0)) {
				v0 = nanValue;
			}
			x[numPairs] = v0;
			y[numPairs] = v1;
			if (!Double.isNaN(v0) && !Double.isNaN(v1)) {
				numPairs++;
			}
		}
		return numPairs;
	}

	private CorrelationMethod createMethod(String methodId, Properties methodProperties) throws AnalysisException {
		if (PearsonCorrelationMethod.ID.equalsIgnoreCase(methodId))
			return new PearsonCorrelationMethod(methodProperties);
		else if (SpearmanCorrelationMethod.ID.equalsIgnoreCase(methodId))
			return new PearsonCorrelationMethod(methodProperties);
		else
			throw new AnalysisException("Unknown correlation method: " + methodId);
	}
}
