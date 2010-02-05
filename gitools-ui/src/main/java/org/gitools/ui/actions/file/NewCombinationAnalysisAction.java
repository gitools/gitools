package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.platform.actions.BaseAction;

public class NewCombinationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = 4604642713057641252L;

	public NewCombinationAnalysisAction() {
		super("Combination analysis ...");
		
		setDesc("Run a combination analysis");
		setMnemonic(KeyEvent.VK_B);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
