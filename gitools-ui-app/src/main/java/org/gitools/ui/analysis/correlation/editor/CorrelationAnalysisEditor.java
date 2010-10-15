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

import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;

public class CorrelationAnalysisEditor extends AnalysisDetailsEditor<CorrelationAnalysis> {

	public CorrelationAnalysisEditor(CorrelationAnalysis analysis) {
		super(analysis, "/vm/analysis/correlation/analysis_details.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {

		PersistenceManager.FileRef fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getData());

		context.put("dataFile",
				fileRef != null ? fileRef.getFile().getName() : "Not defined");

		String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
		context.put("appliedTo", appliedTo);

		if (analysis.getMethod().equals("pearson"))
			context.put("method", "Pearson's correlation");
	}

	@Override
	protected void performUrlAction(String name, Map<String, String> params) {
		if ("NewDataHeatmap".equals(name))
			newDataHeatmap();
		else if ("NewResultsHeatmap".equals(name))
			newResultsHeatmap();
	}

	private void newDataHeatmap() {
		if (analysis.getData() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain data.");
			return;
		}

		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IMatrixView dataTable = new MatrixView(analysis.getData());

		Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
		heatmap.setTitle(analysis.getTitle() + " (data)");

		HeatmapEditor editor = new HeatmapEditor(
				heatmap/*, actions*/);

		editor.setName(editorPanel.deriveName(
				getName(), FileSuffixes.CORRELATIONS,
				"-data", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}

	private void newResultsHeatmap() {
		if (analysis.getResults() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain results.");
			return;
		}

		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IMatrixView dataTable = new MatrixView(analysis.getResults());

		Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
		heatmap.setTitle(analysis.getTitle() + " (results)");

		CorrelationResultsEditor editor = new CorrelationResultsEditor(analysis);

		editor.setName(editorPanel.deriveName(
				getName(), FileSuffixes.CORRELATIONS,
				"-results", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("Heatmap for correlation results created.");
	}
}
