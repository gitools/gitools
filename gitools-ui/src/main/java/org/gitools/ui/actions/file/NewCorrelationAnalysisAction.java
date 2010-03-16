package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;

public class NewCorrelationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8917512377366424724L;

	public NewCorrelationAnalysisAction() {
		super("Correlation analysis ...");
		
		setDesc("Run a correlation analysis");
		setMnemonic(KeyEvent.VK_C);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UnimplementedDialog.show(AppFrame.instance());
	}

}
