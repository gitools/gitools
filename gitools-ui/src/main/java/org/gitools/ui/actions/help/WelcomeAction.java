package org.gitools.ui.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.editor.html.WelcomeEditor;
import org.gitools.ui.platform.AppFrame;


public class WelcomeAction extends BaseAction {

	private static final long serialVersionUID = 6622410876631791866L;

	public WelcomeAction() {
		super("Welcome");
		setDesc("View the welcome");
		setMnemonic(KeyEvent.VK_W);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractEditor view = new WelcomeEditor();
		AppFrame.instance().getEditorsPanel().addEditor(view);
	}
	
}
