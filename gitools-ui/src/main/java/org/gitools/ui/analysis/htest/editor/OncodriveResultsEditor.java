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

package org.gitools.ui.analysis.htest.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JSplitPane;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.actions.BaseAction;

public class OncodriveResultsEditor extends HeatmapEditor {

	protected OncodriveAnalysis analysis;

	protected AbstractTablesPanel tablesPanel;

	protected static Heatmap createHeatmap(OncodriveAnalysis analysis) {
		IMatrixView dataTable = new MatrixView(analysis.getResults());
		Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
		heatmap.setTitle(analysis.getTitle() + " (results)");
		return heatmap;
	}

	protected static List<BaseAction> createToolBar(OncodriveAnalysis analysis) {
		return null;
	}

	public OncodriveResultsEditor(OncodriveAnalysis analysis) {
		super(createHeatmap(analysis), createToolBar(analysis), true);

		tablesPanel = new OncodriveTablesPanel(analysis, heatmap);
		tablesPanel.setMinimumSize(new Dimension(140, 140));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setTopComponent(embeddedContainer);
		splitPane.setBottomComponent(tablesPanel);

		setLayout(new BorderLayout());
		add(splitPane);
	}
}
