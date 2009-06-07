package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;


public class ExportColumnDataAction extends BaseAction {

	private static final long serialVersionUID = -4006822468234729098L;

	public ExportColumnDataAction() {
		super("Export column data");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
