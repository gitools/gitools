package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;

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
