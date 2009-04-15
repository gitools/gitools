package es.imim.bg.ztools.ui.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.WelcomeView;

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
