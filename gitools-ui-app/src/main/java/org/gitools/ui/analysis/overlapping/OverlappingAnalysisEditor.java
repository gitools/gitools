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

package org.gitools.ui.analysis.overlapping;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.xml.OverlappingAnalysisXmlPersistence;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.Map;


public class OverlappingAnalysisEditor extends AnalysisDetailsEditor<OverlappingAnalysis> {

	public OverlappingAnalysisEditor(OverlappingAnalysis analysis) {
		super(analysis, "/vm/analysis/overlapping/analysis_details.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {

        PersistenceManager.FileRef fileRef;

        /*ResourceRef sourceDataRes = analysis.getSourceDataResource();
		context.put("sourceDataFile",
				sourceDataRes != null ? sourceDataRes.getPath() : "Not defined");*/

		fileRef = PersistenceManager.getDefault()
                .getEntityFileRef(analysis.getData());
		context.put("filteredDataFile",
                fileRef != null ? fileRef.getFile().getName() : "Not defined");

		String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
		context.put("appliedTo", appliedTo);

		CutoffCmp cmp = analysis.getBinaryCutoffCmp();
		String filterDesc = cmp == null ?
			"Not filtered"
			: "Binary cutoff filter for values "
				+ cmp.getLongName() + " "
				+ analysis.getBinaryCutoffValue();
		context.put("filterDesc", filterDesc);

        fileRef = PersistenceManager.getDefault()
                .getEntityFileRef(analysis.getResult());
        context.put("resultsFile",
                fileRef != null ? fileRef.getFile().getName() : "Not defined");

        fileRef = PersistenceManager.getDefault()
                .getEntityFileRef(analysis);
        if (fileRef != null) {
            context.put("analysisLocation", fileRef.getFile().getParentFile().getAbsolutePath());
        } else {
            setSaveAllowed(true);
        }

	}

    @Override
    public void doSave(IProgressMonitor monitor) {

        xmlPersistance = new OverlappingAnalysisXmlPersistence();
        fileformat = FileFormats.OVERLAPPING;
        super.doSave(monitor);
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

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from data ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getData());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (data)");

				final HeatmapEditor editor = new HeatmapEditor(heatmap);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.OVERLAPPING,
						"-data", ""));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("New heatmap created.");
					}
				});
			}
		});
	}

	private void newResultsHeatmap() {
		if (analysis.getCellResults() == null) {
			AppFrame.instance().setStatusText("Analysis doesn't contain results.");
			return;
		}

		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Creating new heatmap from results ...", 1);

				IMatrixView dataTable = new MatrixView(analysis.getCellResults());

				Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
				heatmap.setTitle(analysis.getTitle() + " (results)");

				final OverlappingResultsEditor editor = new OverlappingResultsEditor(analysis);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.OVERLAPPING,
						"-results", ""));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("Heatmap for results created.");
					}
				});
			}
		});
	}
}
