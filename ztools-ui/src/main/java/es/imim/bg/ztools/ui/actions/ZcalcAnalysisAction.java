package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.commands.ZCalcCommand;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.file.OpenAnalysisAction;
import es.imim.bg.ztools.ui.jobs.OpenAnalysisJob;
import es.imim.bg.ztools.ui.jobs.ZCalcCommandJob;
import es.imim.bg.ztools.ui.wizards.AnalysisWizard;
import es.imim.bg.ztools.ui.wizards.WizardDataModel;
import es.imim.bg.ztools.ui.wizards.ZCalcAnalysisWizardDialog;

public class ZcalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public ZcalcAnalysisAction() {
		super("ZCalc analysis...");
		
		setDesc("Make a zcalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		AppFrame.instance()
			.setStatusText("Opening Zcalc Analysis Wizard");
		
		ZCalcAnalysisWizardDialog d = new ZCalcAnalysisWizardDialog(AppFrame.instance());
		ZCalcCommand command = d.getCommand();
		WizardDataModel dialogData =  d.getWizardDataModel();
		
		String path = (String) dialogData.getValue(AnalysisWizard.ANALYSIS_WORKING_DIR) + "/" +
		(String) dialogData.getValue(AnalysisWizard.ANALYSIS_NAME);
		File newAnalysis = new File(path);
		
		if (command != null) {
	        ProgressMonitor monitor = new StreamProgressMonitor(System.out, false, false);
	        
	        //execute command
			AppFrame.instance().getJobProcessor().addJob(
					new ZCalcCommandJob(command,monitor,newAnalysis));
		}

	}
}
