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

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationCommand;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.correlation.wizard.CorrelationAnalysisFromFileWizard;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

public class NewCorrelationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8917512377366424724L;

	public NewCorrelationAnalysisAction() {
		super("Correlation analysis ...");
		
		setDesc("Run a correlation analysis");
		setMnemonic(KeyEvent.VK_C);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final CorrelationAnalysisFromFileWizard wizard = new CorrelationAnalysisFromFileWizard();

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final CorrelationAnalysis analysis = wizard.getAnalysis();

		final CorrelationCommand cmd = new CorrelationCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
				wizard.getWorkdir(),
				wizard.getFileName());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final CorrelationAnalysisEditor editor = new CorrelationAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getFileName(wizard.getFileName()) + "." + FileSuffixes.HEATMAP);

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
