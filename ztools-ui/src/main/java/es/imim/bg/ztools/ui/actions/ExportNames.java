package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;

public class ExportNames extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportNames() {
		super("Export names");
		
		setDesc("Export row or column names");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
