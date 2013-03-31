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

package org.gitools.ui.analysis.correlation.editor;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.formats.analysis.CorrelationAnalysisXmlFormat;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.Map;

public class CorrelationAnalysisEditor extends AnalysisDetailsEditor<CorrelationAnalysis> {

	public CorrelationAnalysisEditor(CorrelationAnalysis analysis) {
		super(analysis, "/vm/analysis/correlation/analysis_details.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {

		IResourceLocator dataLocator = analysis.getData().getLocator();
		context.put("dataFile",	dataLocator != null ? dataLocator.getName() : "Not defined");

		String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
		context.put("appliedTo", appliedTo);

		if (analysis.getMethod().equals("pearson"))
			context.put("method", "Pearson's correlation");

        IResourceLocator resultsLocator = analysis.getResults().getLocator();
        context.put("resultsFile", resultsLocator != null ? resultsLocator.getName() : "Not defined");

        IResourceLocator analysisLocator = analysis.getLocator();

        if (analysisLocator != null) {
            context.put("analysisLocation", analysisLocator.getURL());

            if (analysisLocator.isWritable()) {
                setSaveAllowed(true);
            }
        }
	}

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        xmlPersistance = new CorrelationAnalysisXmlFormat();
        fileformat = FileFormats.CORRELATIONS;
        super.doSave(progressMonitor);
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
						getName(), FileSuffixes.CORRELATIONS,
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

				final CorrelationResultsEditor editor = new CorrelationResultsEditor(analysis);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.CORRELATIONS,
						"-results", ""));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("Heatmap for correlation results created.");
					}
				});
			}
		});
	}
}
