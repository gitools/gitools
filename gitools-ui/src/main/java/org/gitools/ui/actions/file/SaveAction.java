package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

public class SaveAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveAction() {
		super("Save");
		setDesc("Save changes");
		setMnemonic(KeyEvent.VK_S);
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return editor != null && editor.isDirty();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
