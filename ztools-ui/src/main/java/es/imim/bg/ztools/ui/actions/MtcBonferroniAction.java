package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;

public class MtcBonferroniAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	public MtcBonferroniAction() {
		super("Bonferroni");
		
		setDesc("Calculate Bonferroni multiple test correction");
		setMnemonic(KeyEvent.VK_B);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AppFrame.instance()
			.setStatusText("Unimplemented action.");
	}

}
