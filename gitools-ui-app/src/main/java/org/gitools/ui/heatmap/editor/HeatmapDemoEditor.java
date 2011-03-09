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

package org.gitools.ui.heatmap.editor;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.ArrayElementAdapter;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;

public class HeatmapDemoEditor extends HeatmapEditor {

	private static final long serialVersionUID = 2467164492764056062L;
	
	public HeatmapDemoEditor(int rows, int cols) {
		super(createModel(rows, cols));
		
		setName("Demo");
	}
	
	private static Heatmap createModel(int rows, int cols) {
		MatrixView matrixView = createTable(rows, cols);
		ElementDecorator decorator = ElementDecoratorFactory.create(
				ElementDecoratorNames.PVALUE, matrixView.getCellAdapter());
		
		return new Heatmap(matrixView, decorator,
				new HeatmapTextLabelsHeader(),
				new HeatmapTextLabelsHeader());
	}

	private static MatrixView createTable(int rows, int cols) {		
		int k = 0;
		DoubleMatrix1D values = DoubleFactory1D.dense.random(2 * rows * cols);
		
		ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				double[] v = new double[] {
					values.getQuick(k++),
					values.getQuick(k++)
				};
				data.setQuick(row, col, v);
			}
		}
		
		final ObjectMatrix1D rowNames = ObjectFactory1D.dense.make(data.rows());
		for (int i = 0; i < rowNames.size(); i++)
			rowNames.setQuick(i, "row " + (i + 1));
		
		final ObjectMatrix1D colNames = ObjectFactory1D.dense.make(data.columns());
		for (int i = 0; i < colNames.size(); i++)
			colNames.setQuick(i, "col " + (i + 1));
		
		ObjectMatrix resultsMatrix = new ObjectMatrix(
				"Demo",
				rowNames,
				colNames,   
				data,
				new ArrayElementAdapter(new String[] {"p-value", "corrected-p-value"}));
		
		HtestAnalysis analysis = new HtestAnalysis();
		analysis.setResults(resultsMatrix);
		
		return new MatrixView(resultsMatrix);
	}
}
