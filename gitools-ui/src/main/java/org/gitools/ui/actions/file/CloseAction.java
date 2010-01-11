package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.EditorsPanel;


public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 2399811452235609343L;

	public CloseAction() {
		super("Close");
		
		setDesc("Close current tab");
		setSmallIconFromResource(IconNames.close16);
		setLargeIconFromResource(IconNames.close24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel workspace = AppFrame.instance().getEditorsPanel();
		AbstractEditor currentView = workspace.getSelectedEditor();
		if (currentView != null)
			workspace.removeEditor(currentView);
		
		AppFrame.instance().refresh();
		AppFrame.instance().setStatusText("View closed.");
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		//TODO Allow Welcome to be closed ???
		return editor != null;
	}
}
