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

package org.gitools.ui.analysis.combination.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.heatmap.editor.HeatmapEditor;

public class CombinationResultsEditor extends HeatmapEditor {

	protected CombinationAnalysis analysis;

	protected CombinationPanel combinationPanel;

	protected static Heatmap createHeatmap(CombinationAnalysis analysis) {
		IMatrixView dataTable = new MatrixView(analysis.getResults());
		Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
		heatmap.setTitle(analysis.getTitle() + " (results)");
		return heatmap;
	}

	public CombinationResultsEditor(CombinationAnalysis analysis) {
		super(createHeatmap(analysis), true);

		combinationPanel = new CombinationPanel(analysis, heatmap);
		combinationPanel.setMinimumSize(new Dimension(100, 100));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setTopComponent(embeddedContainer);
		splitPane.setBottomComponent(combinationPanel);

		setLayout(new BorderLayout());
		add(splitPane);
	}

}
