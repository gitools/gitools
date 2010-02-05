package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.intogen.data.IntogenDataWizard;

public class ImportIntogenTableAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportIntogenTableAction() {
		super("Data table ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new IntogenDataWizard());
		
		wizDlg.open();
	}

}
