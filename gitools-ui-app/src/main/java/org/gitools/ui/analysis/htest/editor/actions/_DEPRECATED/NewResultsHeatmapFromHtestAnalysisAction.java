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

package org.gitools.ui.analysis.htest.editor.actions._DEPRECATED;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.htest.editor._DEPRECATED.HtestAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.actions.ViewRelatedDataFromRowAction;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

@Deprecated
public class NewResultsHeatmapFromHtestAnalysisAction extends BaseAction {

	public NewResultsHeatmapFromHtestAnalysisAction() {
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

		IMatrixView resultsTable = new MatrixView(analysis.getResults());

		ElementDecorator resultsRowDecorator =
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE,
					resultsTable.getCellAdapter());

		List<BaseAction> actions = new ArrayList<BaseAction>();
		if (analysis instanceof EnrichmentAnalysis) {
			EnrichmentAnalysis a = (EnrichmentAnalysis) analysis;
			actions.add(BaseAction.separator);
			actions.add(new ViewRelatedDataFromRowAction(
					a.getTitle(),
					a.getData(),
					a.getModuleMap()));
		}

		Heatmap heatmap = new Heatmap(resultsTable, resultsRowDecorator,
						new HeatmapTextLabelsHeader(), new HeatmapTextLabelsHeader());
		heatmap.setTitle(analysis.getTitle() + " (results)");

		HeatmapEditor editor = new HeatmapEditor(heatmap, actions);

		editor.setName(editorPanel.deriveName(
				currentEditor.getName(), FileSuffixes.ENRICHMENT,
				"-results", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

}
