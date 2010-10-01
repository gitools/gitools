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

package org.gitools.heatmap.util;

import edu.upf.bg.colorscale.util.ColorUtils;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapCluster;
import org.gitools.heatmap.model.HeatmapClusterSet;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.HeatmapHeaderDecoration;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.BinaryElementDecorator;


public class HeatmapUtil {

	public static Heatmap createFromMatrixView(IMatrixView matrixView) {
		final IMatrix matrix = matrixView.getContents();

		final Heatmap figure = new Heatmap(matrixView);

		if (matrix instanceof DoubleBinaryMatrix) {
			BinaryElementDecorator decorator =
				(BinaryElementDecorator) ElementDecoratorFactory.create(
						ElementDecoratorNames.BINARY,
						matrix.getCellAdapter());
			decorator.setCutoff(1.0);
			decorator.setCutoffCmp(CutoffCmp.EQ);
			figure.setCellDecorator(decorator);
			figure.setRowsGridEnabled(false);
			figure.setColumnsGridEnabled(false);
		} else if (matrix instanceof DoubleMatrix) {
			figure.setRowsGridEnabled(false);
			figure.setColumnsGridEnabled(false);
		}
		else if (matrix instanceof ObjectMatrix) {
			ElementDecorator decorator =
				ElementDecoratorFactory.create(
						ElementDecoratorNames.PVALUE,
						matrix.getCellAdapter());
			figure.setCellDecorator(decorator);
		}

		return figure;
	}
}
