package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.OncozAnalysisWizard;

public class NewOncozAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewOncozAnalysisAction() {
		super("Oncoz analysis ...");

		setDesc("Run an oncoz analysis");
		setMnemonic(KeyEvent.VK_O);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new OncozAnalysisWizard());
		
		wizDlg.open();
	}
}
