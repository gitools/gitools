package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.commands.ZCalcCommand;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.ProgressMonitorDialog;
import es.imim.bg.ztools.ui.jobs.ZCalcCommandJob;
import es.imim.bg.ztools.ui.wizard.AnalysisWizard;
import es.imim.bg.ztools.ui.wizard.WizardDataModel;
import es.imim.bg.ztools.ui.wizard.zetcalc.ZCalcAnalysisWizard;

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
