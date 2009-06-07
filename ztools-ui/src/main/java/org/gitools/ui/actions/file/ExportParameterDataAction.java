package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;


public class ExportParameterDataAction extends BaseAction {

	private static final long serialVersionUID = 8522350911282608431L;

	public ExportParameterDataAction() {
		super("Export parameter data");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
