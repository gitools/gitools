package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;

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
