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
import java.util.ArrayList;
import java.util.List;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.persistence.FileFormat;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.htest.editor.HtestAnalysisEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFileWizard;

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

		/*SaveFileWizard wiz = SaveFileWizard.createSimple(
				"New heatmap from analysis results",
				editorPanel.createName(),
				Settings.getDefault().getLastPath(),
				new FileFormat[] {new FileFormat("Heatmap", FileSuffixes.HEATMAP_FIGURE)});

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
		dlg.setVisible(true);
		if (dlg.isCancelled())
			return;

		Settings.getDefault().setLastPath(wiz.getFolder());*/

		HtestAnalysis analysis = (HtestAnalysis) currentEditor.getModel();

		IMatrixView resultsTable = new MatrixView(analysis.getResultsMatrix());

		ElementDecorator resultsRowDecorator =
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE,
					resultsTable.getCellAdapter());

		List<BaseAction> actions = new ArrayList<BaseAction>();
		if (analysis instanceof EnrichmentAnalysis) {
			EnrichmentAnalysis a = (EnrichmentAnalysis) analysis;
			actions.add(BaseAction.separator);
			actions.add(new ViewAnnotatedElementsHeatmapAction(
					a.getTitle(),
					a.getDataMatrix(),
					a.getModuleMap()));
		}

		Heatmap heatmap = new Heatmap(resultsTable, resultsRowDecorator,
						new HeatmapHeader(), new HeatmapHeader());
		heatmap.setTitle(analysis.getTitle() + " (results)");

		HeatmapEditor resultsEditor = new HeatmapEditor(heatmap, actions);

		resultsEditor.setName(editorPanel.createName(
				EditorsPanel.DEFAULT_NAME_PREFIX,
				"." + FileSuffixes.HEATMAP_FIGURE));

		//resultsEditor.setFile(wiz.getFile());

		editorPanel.addEditor(resultsEditor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

}
