package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;

public class OpenDataAction extends BaseAction {

	private static final long serialVersionUID = 6300005223590277333L;

	public OpenDataAction() {
		super("Data ...");
		setMnemonic(KeyEvent.VK_D);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = getSelectedFile("Select file");
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();
		}
			
	}

}
