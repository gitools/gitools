package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
