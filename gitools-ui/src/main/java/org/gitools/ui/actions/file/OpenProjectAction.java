package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.actions.UnimplementedAction;

public class OpenProjectAction extends BaseAction {

	private static final long serialVersionUID = -8008209863785533045L;

	public OpenProjectAction() {
		super("Project ...");
		setMnemonic(KeyEvent.VK_P);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new UnimplementedAction().actionPerformed(e);
	}

}
