package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog;
import es.imim.bg.ztools.ui.dialogs.ZCalcAnalysisDialog;

public class ZcalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public ZcalcAnalysisAction() {
		super("ZCalc analysis...");
		
		setDesc("Make a zcalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ZCalcAnalysisDialog d = new ZCalcAnalysisDialog(AppFrame.instance(), "test", "test");
		d.setVisible(true);
		
		AppFrame.instance()
			.setStatusText("Action in development.");
	}

}
