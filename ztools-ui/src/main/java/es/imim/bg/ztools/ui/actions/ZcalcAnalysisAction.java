package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;

public class ZcalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public ZcalcAnalysisAction() {
		super("ZCalc...");
		
		setDesc("Make a zcalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
