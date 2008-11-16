package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ExportNames extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportNames() {
		super("Export names");
		
		setDesc("Export row or column names");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
