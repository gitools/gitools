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
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.SwingUtilities;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

public class CombinationsAction extends BaseAction {

	public CombinationsAction() {
		super("Combinations");
		setDesc("Combinations");
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

		final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();
		wizard.setExamplePageEnabled(false);
		wizard.setDataFromMemory(true);
		wizard.setAttributes(matrixView.getCellAttributes());
		wizard.setSaveFilePageEnabled(false);

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final CombinationAnalysis analysis = wizard.getAnalysis();

		analysis.setData(matrixView);
		
		File columnSetsFile = wizard.getColumnSetsPage().getFile();
		String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
		String columnSetsMime = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getMime() : null;

		final CombinationCommand cmd = new CombinationCommand(
				analysis,
				null, null,
				columnSetsMime,
				columnSetsPath,
				null, null);
		cmd.setStoreAnalysis(false);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

					String ext = PersistenceUtils.getExtension(currentEditor.getName());
					editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "-combination", FileSuffixes.HEATMAP));

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
