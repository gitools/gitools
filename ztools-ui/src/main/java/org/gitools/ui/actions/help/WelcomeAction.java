package org.gitools.ui.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.views.AbstractView;
import org.gitools.ui.views.WelcomeView;


public class WelcomeAction extends BaseAction {

	private static final long serialVersionUID = 6622410876631791866L;

	public WelcomeAction() {
		super("Welcome");
		setDesc("View the welcome");
		setMnemonic(KeyEvent.VK_W);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = new WelcomeView();
		AppFrame.instance().getWorkspace().addView(view);
	}
	
}
