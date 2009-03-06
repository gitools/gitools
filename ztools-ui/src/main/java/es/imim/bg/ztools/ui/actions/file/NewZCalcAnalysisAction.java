package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.security.acl.Owner;
import java.util.zip.DataFormatException;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;
import es.imim.bg.ztools.commands.ZCalcCommand;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.ProgressMonitorDialog;
import es.imim.bg.ztools.ui.jobs.OpenAnalysisJob;
import es.imim.bg.ztools.ui.jobs.ZCalcCommandJob;
import es.imim.bg.ztools.ui.wizard.AnalysisWizard;
import es.imim.bg.ztools.ui.wizard.WizardDataModel;
import es.imim.bg.ztools.ui.wizard.zetcalc.ZCalcAnalysisWizard;

public class NewZCalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewZCalcAnalysisAction() {
		super("ZCalc analysis...");
		
		setDesc("Make a zcalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		AppFrame.instance()
			.setStatusText("Opening Zcalc Analysis Wizard");
		
		ZCalcAnalysisWizard d = new ZCalcAnalysisWizard(AppFrame.instance());
		ZCalcCommand command = d.getCommand();
		WizardDataModel dialogData =  d.getWizardDataModel();
		
		String path = (String) dialogData.getValue(AnalysisWizard.ANALYSIS_WORKING_DIR) + "/" +
		(String) dialogData.getValue(AnalysisWizard.ANALYSIS_NAME);
		File newAnalysis = new File(path);
		
		if (command != null) {
	        
	        final ProgressMonitorDialog pmd = new ProgressMonitorDialog(AppFrame.instance(), "Calculating");
	        pmd.setVisible(true);
	        final JTextArea textArea = pmd.getTextArea();
	        ProgressMonitor monitor = new StreamProgressMonitor(System.out, false, false) {
	        		        	
	        	@Override
	        	protected void print(final String text) {
	        		try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
				        		System.err.println(text);
					        	pmd.addText(text);
							}
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        };
	        
	        //execute command
			AppFrame.instance().getJobProcessor().addJob(
					new ZCalcCommandJob(command, monitor, newAnalysis));
			System.out.println(textArea.getText());
		}

	}
}
