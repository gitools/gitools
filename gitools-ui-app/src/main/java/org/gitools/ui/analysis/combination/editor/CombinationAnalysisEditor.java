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

package org.gitools.ui.analysis.combination.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.apache.velocity.VelocityContext;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class CombinationAnalysisEditor extends AnalysisDetailsEditor<CombinationAnalysis> {

	public CombinationAnalysisEditor(CombinationAnalysis analysis) {
		super(analysis, "/vm/analysis/combination/analysis_details.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {
		String combOf = "columns";
		if (analysis.isTransposeData())
			combOf = "rows";
		context.put("combinationOf", combOf);

		PersistenceManager.FileRef fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getData());

		context.put("dataFile",
				fileRef != null ? fileRef.getFile().getName() : "Not defined");

		fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getGroupsMap());

		String groupsFile = fileRef != null ? fileRef.getFile().getName()
				: "Not specified. All " + combOf + " are combined";
		context.put("groupsFile", groupsFile);

		String sizeAttr = analysis.getSizeAttrName();
		if (sizeAttr == null || sizeAttr.isEmpty())
			sizeAttr = "Constant value of 1";
		context.put("sizeAttr", sizeAttr);

		String pvalueAttr = analysis.getPvalueAttrName();
		if (pvalueAttr == null || pvalueAttr.isEmpty()) {
			List<IElementAttribute> attrs = analysis.getData().getCellAttributes();
			if (attrs.size() > 0)
				pvalueAttr = attrs.get(0).getName();
		}
		context.put("pvalueAttr", pvalueAttr);
	}

	@Override
	protected void performUrlAction(String name, Map<String, String> params) {
		if ("NewDataHeatmap".equals(name))
			newDataHeatmap();
		else if ("ViewModuleMap".equals(name))
			viewModuleMap();
		else if ("NewResultsHeatmap".equals(name))
			newResultsHeatmap();
	}

	private void newDataHeatmap() {
		if (analysis.getData() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain data.");
			return;
		}

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from data ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getData());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (data)");

				final HeatmapEditor editor = new HeatmapEditor(heatmap);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.COMBINATION,
						"-data", FileSuffixes.HEATMAP));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("New heatmap created.");
					}
				});
			}
		});
	}

	private void viewModuleMap() {
		UnimplementedDialog.show(AppFrame.instance());
	}

	private void newResultsHeatmap() {
		if (analysis.getResults() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain results.");
			return;
		}

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from results ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getResults());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (results)");

				final CombinationResultsEditor editor = new CombinationResultsEditor(analysis);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.COMBINATION,
						"-results", FileSuffixes.HEATMAP));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("Heatmap for combination results created.");
					}
				});
			}
		});
	}
}
