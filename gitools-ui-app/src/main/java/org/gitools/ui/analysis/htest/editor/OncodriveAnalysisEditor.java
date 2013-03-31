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

package org.gitools.ui.analysis.htest.editor;

import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.ToolConfig;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class OncodriveAnalysisEditor extends AnalysisDetailsEditor<OncodriveAnalysis> {

	public OncodriveAnalysisEditor(OncodriveAnalysis analysis) {
		super(analysis, "/vm/analysis/oncodrive/analysis_details.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {

		IResourceLocator fileRef = analysis.getData().getLocator();

		context.put("dataFile",
				fileRef != null ? fileRef.getName() : "Not defined");

        ToolConfig testConfig = analysis.getTestConfig();
        if (testConfig.get(TestFactory.TEST_NAME_PROPERTY) != "") {
            context.put("test", WordUtils.capitalize(testConfig.get(TestFactory.TEST_NAME_PROPERTY)));
            HashMap<String,Object> testAttributes = new HashMap<String,Object>();
            for (String key: testConfig.getConfiguration().keySet()) {
                if (!key.equals(TestFactory.TEST_NAME_PROPERTY))
                    testAttributes.put(WordUtils.capitalize(key),
                            WordUtils.capitalize(testConfig.get(key)));
            }
            if (testAttributes.size() > 0)
                context.put("testAttributes",testAttributes);

        }

		CutoffCmp cmp = analysis.getBinaryCutoffCmp();
		String filterDesc = cmp == null ?
			"Not filtered"
			: "Binary cutoff filter for values "
				+ cmp.getLongName() + " "
				+ analysis.getBinaryCutoffValue();
		context.put("filterDesc", filterDesc);

		fileRef = analysis.getModuleMap().getLocator();

		context.put("modulesFile",
				fileRef != null ? fileRef.getName() : "Unknown");

		context.put("moduleMinSize", analysis.getMinModuleSize());
		int maxSize = analysis.getMaxModuleSize();
		context.put("moduleMaxSize", maxSize != Integer.MAX_VALUE ? maxSize : "No limit");

		if (analysis.getMtc().equals("bh"))
			context.put("mtc", "Benjamini Hochberg FDR");
		else if (analysis.getMtc().equals("bonferroni"))
			context.put("mtc", "Bonferroni");

        fileRef = analysis.getResults().getLocator();
        context.put("resultsFile",
                fileRef != null ? fileRef.getName() : "Not defined");

        fileRef = analysis.getLocator();
        if (fileRef != null) {
            context.put("analysisLocation", fileRef.getURL());
        } else {
            setSaveAllowed(true);
        }

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
                String testName = analysis.getTestConfig().getConfiguration().get(TestFactory.TEST_NAME_PROPERTY);
                if (!testName.equals(TestFactory.ZSCORE_TEST)) {
                    //entry data is binary
                    ElementDecorator[] decorators = new ElementDecorator[1];
                    decorators[0] = new BinaryElementDecorator(heatmap.getActiveCellDecorator().getAdapter());
                    heatmap.setCellDecorators(decorators);
                }
				heatmap.setTitle(analysis.getTitle() + " (data)");

				final HeatmapEditor editor = new HeatmapEditor(heatmap);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.ONCODRIVE,
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

				final OncodriveResultsEditor editor = new OncodriveResultsEditor(analysis);

				editor.setName(editorPanel.deriveName(
						getName(), FileSuffixes.ONCODRIVE,
						"-results", ""));

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						editorPanel.addEditor(editor);
						AppFrame.instance().setStatusText("Heatmap for oncodrive results created.");
					}
				});
			}
		});
	}
}
