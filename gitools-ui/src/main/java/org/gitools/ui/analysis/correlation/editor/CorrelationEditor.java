/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.analysis.correlation.editor;

import java.awt.Color;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.ui.heatmap.editor.HeatmapEditor;

public class CorrelationEditor extends HeatmapEditor {

	public CorrelationEditor(CorrelationAnalysis analysis) {
		super(createHeatmap(analysis));
	}

	private static Heatmap createHeatmap(CorrelationAnalysis analysis) {
		IMatrixView results = new MatrixView(analysis.getResults());
		Heatmap heatmap = new Heatmap(results);
		IElementAdapter cellAdapter = results.getCellAdapter();
		LinearTwoSidedElementDecorator dec = new LinearTwoSidedElementDecorator(cellAdapter);
		dec.setMinColor(Color.GREEN);
		dec.setMidColor(Color.WHITE);
		dec.setMaxColor(new Color(255, 0, 255));
		int valueIndex = cellAdapter.getPropertyIndex("score");
		dec.setValueIndex(valueIndex != -1 ? valueIndex : 0);
		dec.setMinValue(-1);
		dec.setMaxValue(1);
		heatmap.setCellDecorator(dec);

		heatmap.setTitle(analysis.getTitle());

		return heatmap;
	}
}
