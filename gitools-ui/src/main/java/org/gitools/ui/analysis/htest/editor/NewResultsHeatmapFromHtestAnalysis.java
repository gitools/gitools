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

import java.awt.event.ActionEvent;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

public class NewResultsHeatmapFromHtestAnalysis extends BaseAction {

	public NewResultsHeatmapFromHtestAnalysis() {
		super("New heatmap from results");

		setDesc("New heatmap from results");
		setSmallIconFromResource(IconNames.newResultsHeatmap16);
		setLargeIconFromResource(IconNames.newResultsHeatmap24);

		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currentEditor = editorPanel.getSelectedEditor();
		if (!(currentEditor instanceof HtestAnalysisEditor))
			return;

		HtestAnalysis analysis = (HtestAnalysis) currentEditor.getModel();

		IMatrixView resultsTable = new MatrixView(analysis.getResultsMatrix());

		ElementDecorator resultsRowDecorator =
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE,
					resultsTable.getCellAdapter());

		HeatmapEditor resultsEditor = new HeatmapEditor(
				new Heatmap(resultsTable, resultsRowDecorator,
						new HeatmapHeader(), new HeatmapHeader()));

		resultsEditor.setName(currentEditor.getName() + " (results)");

		editorPanel.addEditor(resultsEditor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

}
