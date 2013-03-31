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

package org.gitools.heatmap.util;

import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.BinaryElementDecorator;


public class HeatmapUtil {

	public static Heatmap createFromMatrixView(IMatrixView matrixView) {
		final Heatmap figure = new Heatmap(matrixView);
		final int propertiesNb = figure.getMatrixView().getCellAdapter().getPropertyCount();

		while (matrixView.getContents() instanceof MatrixView)
			matrixView = (IMatrixView) matrixView.getContents();
		final IMatrix matrix = matrixView.getContents();

		if (matrix instanceof DoubleBinaryMatrix) {
			BinaryElementDecorator[] decorators = new BinaryElementDecorator[propertiesNb];
			for (int i = 0; i < decorators.length; i++) {
				BinaryElementDecorator decorator =
					(BinaryElementDecorator) ElementDecoratorFactory.create(
							ElementDecoratorNames.BINARY,
							matrix.getCellAdapter());
                decorator.setValueIndex(i);
				decorator.setCutoff(1.0);
				decorator.setCutoffCmp(CutoffCmp.EQ);
				decorators[i] = decorator;
			}
			figure.setCellDecorators(decorators);
			figure.getRowDim().setGridEnabled(false);
			figure.getColumnDim().setGridEnabled(false);
        }
        else if (matrix instanceof DoubleMatrix) {
			figure.getRowDim().setGridEnabled(false);
			figure.getColumnDim().setGridEnabled(false);
		}
		/*else if (matrix instanceof ObjectMatrix) {
		}*/

		return figure;
	}
}
