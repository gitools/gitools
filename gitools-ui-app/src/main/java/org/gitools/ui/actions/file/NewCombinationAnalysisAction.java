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

package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class NewCombinationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = 4604642713057641252L;

	public NewCombinationAnalysisAction() {
		super("Combination analysis ...");
		
		setDesc("Run a combination analysis");
		setMnemonic(KeyEvent.VK_B);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final CombinationAnalysis analysis = wizard.getAnalysis();

		final String analysisPath = wizard.getSaveFilePage().getFileName();
		File columnSetsFile = wizard.getColumnSetsPage().getFile();
		String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
		String columnSetsMime = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getMime() : null;

		final CombinationCommand cmd = new CombinationCommand(
				analysis,
				wizard.getDataFilePage().getFileFormat().getMime(),
				wizard.getDataFilePage().getFile().getAbsolutePath(),
				columnSetsMime,
				columnSetsPath,
				wizard.getSaveFilePage().getFolder(),
				analysisPath);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getFileName(analysisPath) + "." + FileSuffixes.HEATMAP);

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
