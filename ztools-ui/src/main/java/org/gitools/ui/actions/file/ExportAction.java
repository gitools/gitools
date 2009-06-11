package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.ExportDialog;

public class ExportAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportAction() {
		super("Export...");
		
		setDesc("Export...");
		setMnemonic(KeyEvent.VK_X);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		ExportDialog dlg = new ExportDialog(AppFrame.instance());
		dlg.setVisible(true);
		
		AppFrame.instance().setStatusText("Done.");
	}
}
