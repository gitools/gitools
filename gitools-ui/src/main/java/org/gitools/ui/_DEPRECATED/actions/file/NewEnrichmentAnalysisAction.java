package org.gitools.ui._DEPRECATED.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.analysis.htest.enrichment.EnrichmentCommand;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.dialog.ProgressMonitorDialog;
import org.gitools.ui.jobs.ZCalcCommandJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.WizardDataModel;
import org.gitools.ui.wizardmess.zetcalc.ZCalcAnalysisWizard;

import edu.upf.bg.progressmonitor.IProgressMonitor;

@Deprecated
public class NewEnrichmentAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewEnrichmentAnalysisAction() {
		super("Enrichment analysis (old wizard) ...");

		setDesc("Run an enrichment analysis");
		setMnemonic(KeyEvent.VK_E);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final ZCalcAnalysisWizard wizard = 
			new ZCalcAnalysisWizard(AppFrame.instance());

		final WizardDataModel dialogData = 
			wizard.getWizardDataModel();

		final EnrichmentCommand command = wizard.getCommand();

		final String workDir = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_WORKING_DIR);
		final String analysisName = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_NAME);

		final File analysisPath = new File(workDir, analysisName);

		if (command != null) {

			final ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(
					AppFrame.instance(), "Log");

			final IProgressMonitor monitor = 
				monitorDialog.getProgressMonitor();

			AppFrame.instance().getJobProcessor().addJob(
					new ZCalcCommandJob(command, monitor, analysisPath));
			
			monitorDialog.setVisible(true);
		}
	}
}
