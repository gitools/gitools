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

package org.gitools.ui.actions.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.analysis.overlapping.wizard.OverlappingAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OverlappingsAction extends BaseAction {

	public OverlappingsAction() {
		super("Overlapping");
		setDesc("Overlapping analysis");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		final IEditor currentEditor = editorPanel.getSelectedEditor();

		IMatrixView matrixView = ActionUtils.getMatrixView();

		if (matrixView == null)
			return;

		final OverlappingAnalysisWizard wiz = new OverlappingAnalysisWizard();
		wiz.setExamplePageEnabled(false);
		wiz.setDataFromMemory(true);
		wiz.setAttributes(matrixView.getCellAttributes());
		wiz.setSaveFilePageEnabled(false);

		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);

		dlg.open();

		if (dlg.isCancelled())
			return;

		final OverlappingAnalysis analysis = wiz.getAnalysis();

		if (!analysis.isTransposeData()) {
			if (matrixView.getSelectedColumns().length > 0) {
				MatrixView mv = new MatrixView(matrixView);
				mv.visibleColumnsFromSelection();
				matrixView = mv;
			}
		}
		else {
			if (matrixView.getSelectedRows().length > 0) {
				MatrixView mv = new MatrixView(matrixView);
				mv.visibleRowsFromSelection();
				matrixView = mv;
			}
		}

		analysis.setData(matrixView);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					new OverlappingProcessor(analysis).run(monitor);

					if (monitor.isCancelled())
						return;

					final OverlappingAnalysisEditor editor = new OverlappingAnalysisEditor(analysis);

					String ext = PersistenceUtils.getExtension(currentEditor.getName());

                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals(""))
                        editor.setName(analysis.getTitle() + "."+FileSuffixes.OVERLAPPING);
                    else
                        editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "", FileSuffixes.OVERLAPPING));
                    editor.abbreviateName(Settings.getDefault().getEditorTabLength());

                    SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AppFrame.instance().getEditorsPanel().addEditor(editor);
							AppFrame.instance().refresh();
						}
					});

					monitor.end();

					AppFrame.instance().setStatusText("Done.");
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}
}
