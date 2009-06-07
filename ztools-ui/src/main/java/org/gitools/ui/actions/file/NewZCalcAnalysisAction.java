package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.ProgressMonitorDialog;
import org.gitools.ui.jobs.ZCalcCommandJob;
import org.gitools.ui.wizard.AnalysisWizard;
import org.gitools.ui.wizard.WizardDataModel;
import org.gitools.ui.wizard.zetcalc.ZCalcAnalysisWizard;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.commands.ZCalcCommand;

public class NewZCalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewZCalcAnalysisAction() {
		super("New ZetCalc analysis...");

		setDesc("Run a new ZetCalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final ZCalcAnalysisWizard wizard = 
			new ZCalcAnalysisWizard(AppFrame.instance());

		final WizardDataModel dialogData = 
			wizard.getWizardDataModel();

		final ZCalcCommand command = wizard.getCommand();

		final String workDir = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_WORKING_DIR);
		final String analysisName = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_NAME);

		final File analysisPath = new File(workDir, analysisName);

		if (command != null) {

			final ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(
					AppFrame.instance(), "Log");

			final ProgressMonitor monitor = 
				monitorDialog.getProgressMonitor();

			AppFrame.instance().getJobProcessor().addJob(
					new ZCalcCommandJob(command, monitor, analysisPath));
			
			monitorDialog.setVisible(true);
		}
	}
}
