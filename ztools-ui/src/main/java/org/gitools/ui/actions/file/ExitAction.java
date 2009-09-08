package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;


public class ExitAction extends BaseAction {

	private static final long serialVersionUID = -2861462318817904958L;

	public ExitAction() {
		super("Exit");
		
		setDesc("Close aplication");
		setMnemonic(KeyEvent.VK_X);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//TODO: Ask confirmation !
		
		Options.instance().save();
		System.exit(0);
	}

}
