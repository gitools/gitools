package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AboutDialog;

public class AboutAction extends BaseAction {

	private static final long serialVersionUID = 8302818623988394433L;

	public AboutAction() {
		super("About ztools...");
		
		setDesc("Know more about this application");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new AboutDialog(AppFrame.instance())
			.setVisible(true);
	}

}