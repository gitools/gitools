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
import java.io.File;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentCommand;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.EnrichmentAnalysisWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class NewEnrichmentAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewEnrichmentAnalysisAction() {
		super("Enrichment analysis ...");

		setDesc("Run an enrichment analysis");
		setMnemonic(KeyEvent.VK_E);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final EnrichmentAnalysisWizard wizard = new EnrichmentAnalysisWizard();
		
		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);
		
		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final EnrichmentAnalysis analysis = wizard.getAnalysis();

		File populationFile = wizard.getPopulationFile();
		
		final EnrichmentCommand cmd = new EnrichmentCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
                wizard.getSelectedValueIndex(),
				populationFile != null ? populationFile.getAbsolutePath() : null,
				wizard.getPopulationDefaultValue(),
				wizard.getModulesFileMime(),
				wizard.getModulesFile().getAbsolutePath(),
				wizard.getWorkdir(),
				wizard.getFileName());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final EnrichmentAnalysisEditor editor = new EnrichmentAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getBaseName(wizard.getFileName()));

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AppFrame.instance().getEditorsPanel().addEditor(editor);
							AppFrame.instance().refresh();
						}
					});

					monitor.end();

					AppFrame.instance().setStatusText("Ok.");
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}
}
