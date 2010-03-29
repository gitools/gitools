package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import org.gitools.ui.IconNames;
import org.gitools.ui.intogen.dialog.IntogenImportDialog;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

public class ImportIntogenMatrixAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportIntogenMatrixAction() {
		super("Matrix ...");
		setLargeIconFromResource(IconNames.intogen24);
		setSmallIconFromResource(IconNames.intogen16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new IntogenDataWizard());
		
		wizDlg.open();*/

		IntogenImportDialog dlg = new IntogenImportDialog(AppFrame.instance(),
				IntogenImportDialog.ImportType.ONCODATA);
		
		dlg.setVisible(true);
	}

}
