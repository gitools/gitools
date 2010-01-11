package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.actions.UnimplementedAction;
import org.gitools.ui.platform.editor.IEditor;

public class SaveAsAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveAsAction() {
		super("Save As ...");
		setDesc("Save to another location");
		setMnemonic(KeyEvent.VK_A);
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return editor != null && editor.isSaveAsAllowed();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new UnimplementedAction().actionPerformed(e);
	}
}
