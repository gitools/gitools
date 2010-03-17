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

package org.gitools.ui.analysis.htest.editor.actions;

import java.awt.event.ActionEvent;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.htest.editor.HtestAnalysisEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

public class NewDataHeatmapFromHtestAnalysisAction extends BaseAction {

	public NewDataHeatmapFromHtestAnalysisAction() {
		super("New heatmap from data");

		setDesc("New heatmap from data");
		setSmallIconFromResource(IconNames.newDataHeatmap16);
		setLargeIconFromResource(IconNames.newDataHeatmap24);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currentEditor = editorPanel.getSelectedEditor();
		if (!(currentEditor instanceof HtestAnalysisEditor))
			return;

		HtestAnalysis analysis = (HtestAnalysis) currentEditor.getModel();

		if (analysis.getDataMatrix() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contains data.");
			return;
		}
		
		IMatrixView dataTable = new MatrixView(analysis.getDataMatrix());

		Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
		heatmap.setTitle(analysis.getTitle() + " (data)");

		/*List<BaseAction> actions = new ArrayList<BaseAction>();
		if (analysis instanceof EnrichmentAnalysis) {
			EnrichmentAnalysis a = (EnrichmentAnalysis) analysis;
			actions.add(BaseAction.separator);
			actions.add(new ViewAnnotatedElementsHeatmapAction(dataTable, a.getModuleMap()));
		}*/

		HeatmapEditor editor = new HeatmapEditor(
				heatmap/*, actions*/);

		editor.setName(editorPanel.deriveName(
				currentEditor.getName(), FileSuffixes.ENRICHMENT,
				"-data", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

}
