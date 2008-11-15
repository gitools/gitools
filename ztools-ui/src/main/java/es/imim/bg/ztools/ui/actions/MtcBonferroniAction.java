package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MtcBonferroniAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	public MtcBonferroniAction() {
		super("Bonferroni");
		
		setDesc("Calculate Bonferroni correction");
		setMnemonic(KeyEvent.VK_B);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
