package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;


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
		EditorsPanel editorsPanel = AppFrame.instance().getEditorsPanel();
		AbstractEditor currentEditor = editorsPanel.getSelectedEditor();
		if (currentEditor != null)
			editorsPanel.removeEditor(currentEditor);
		
		AppFrame.instance().refresh();
		AppFrame.instance().setStatusText("View closed.");
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return editor != null;
	}
}
