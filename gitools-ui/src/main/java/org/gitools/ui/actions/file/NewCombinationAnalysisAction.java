package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;

public class NewCombinationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = 4604642713057641252L;

	public NewCombinationAnalysisAction() {
		super("Combination analysis ...");
		
		setDesc("Run a combination analysis");
		setMnemonic(KeyEvent.VK_B);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UnimplementedDialog.show(AppFrame.instance());
	}

}
