package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.OncozAnalysisWizard;
import org.gitools.ui.dialog.UnimplementedDialog;

public class NewOncozAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewOncozAnalysisAction() {
		super("OncoDriver analysis ...");

		setDesc("Run an oncodriver analysis");
		setMnemonic(KeyEvent.VK_O);
		
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		UnimplementedDialog.show(AppFrame.instance());
		if (true) return;

		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new OncozAnalysisWizard());
		
		wizDlg.open();
	}
}
