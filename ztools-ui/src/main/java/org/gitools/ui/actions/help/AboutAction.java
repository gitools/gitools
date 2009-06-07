package org.gitools.ui.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.AboutDialog;


public class AboutAction extends BaseAction {

	private static final long serialVersionUID = 8302818623988394433L;

	public AboutAction() {
		super("About " + AppFrame.getAppName() + "...");
		setDesc("Know more about this application");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new AboutDialog(AppFrame.instance())
			.setVisible(true);
	}

}
