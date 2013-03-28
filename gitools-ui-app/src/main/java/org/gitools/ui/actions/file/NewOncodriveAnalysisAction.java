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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveCommand;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.OncodriveAnalysisWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class NewOncodriveAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewOncodriveAnalysisAction() {
		super("OncoDrive analysis ...");

		setDesc("Run an oncodrive analysis");
		setMnemonic(KeyEvent.VK_O);
		
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//UnimplementedDialog.show(AppFrame.instance());
		//if (true) return;

		final OncodriveAnalysisWizard wizard = new OncodriveAnalysisWizard();

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final OncodriveAnalysis analysis = wizard.getAnalysis();

		File populationFile = wizard.getPopulationFile();
		File modulesFile = wizard.getModulesFile();

		final OncodriveCommand cmd = new OncodriveCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
                wizard.getSelectedValueIndex(),
				populationFile != null ? populationFile.getAbsolutePath() : null,
				wizard.getPopulationDefaultValue(),
				wizard.getModulesFileMime(),
				modulesFile != null ? modulesFile.getAbsolutePath() : null,
				wizard.getWorkdir(),
				wizard.getFileName());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final OncodriveAnalysisEditor editor = new OncodriveAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getBaseName(wizard.getFileName()));

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
