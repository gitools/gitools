package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.export.ExportWizard;

public class ExportAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportAction() {
		super("Export...");
		
		setDesc("Export...");
		setMnemonic(KeyEvent.VK_X);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new ExportWizard());
		
		wizDlg.open();
		
		AppFrame.instance().setStatusText("Done.");
	}
}
