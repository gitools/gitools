package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import org.gitools.ui.intogen.dialog.IntogenImportDialog;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

public class ImportIntogenOncomodulesAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportIntogenOncomodulesAction() {
		super("Oncomodules ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new IntogenOncomodulesWizard());
		
		wizDlg.open();*/

		/*IntogenOncomodulesPage editor = new IntogenOncomodulesPage();
		AppFrame.instance().getEditorsPanel().addEditor(editor);*/

		IntogenImportDialog dlg = new IntogenImportDialog(AppFrame.instance(),
				IntogenImportDialog.ImportType.ONCOMODULES);

		dlg.setVisible(true);
	}

}
