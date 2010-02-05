package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenDataAction extends BaseAction {

	private static final long serialVersionUID = 6300005223590277333L;

	public OpenDataAction() {
		super("Data ...");
		setMnemonic(KeyEvent.VK_D);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = FileChooserUtils.selectFile(
				"Select file",
				FileChooserUtils.MODE_OPEN);
		
		if (selectedPath != null) {
			Settings.getDefault().setLastPath(selectedPath.getParent());
			Settings.getDefault().save();
		}
			
	}

}
