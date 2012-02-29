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

import edu.upf.bg.cutoffcmp.CutoffCmp;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.decorator.ElementDecorator;
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
		} else if (matrix instanceof DoubleMatrix) {
			ElementDecorator[] decorators = new ElementDecorator[propertiesNb];
			for (int i = 0; i < decorators.length; i++) {
				ElementDecorator decorator =
					ElementDecoratorFactory.create(
						ElementDecoratorNames.PVALUE,
						matrix.getCellAdapter());
                decorator.setValueIndex(i);
			decorators[i] = decorator;
			}
			figure.setCellDecorators(decorators);
			figure.getRowDim().setGridEnabled(false);
			figure.getColumnDim().setGridEnabled(false);
		}
		else if (matrix instanceof ObjectMatrix) {
			
			ElementDecorator[] decorators = new ElementDecorator[propertiesNb];
			for (int i = 0; i < decorators.length; i++) {
				ElementDecorator decorator =
					ElementDecoratorFactory.create(
						ElementDecoratorNames.PVALUE,
						matrix.getCellAdapter());
                decorator.setValueIndex(i);
			decorators[i] = decorator;
			}
			figure.setCellDecorators(decorators);
		}

		return figure;
	}
}
