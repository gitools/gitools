package es.imim.bg.ztools.ui.dialogs;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ZCalcAnalysisDialog extends AnalysisDialog {

	private static final long serialVersionUID = -5440110772686552307L;

	public ZCalcAnalysisDialog(JFrame owner, String windowTitle, String dialogTitle) {
		super(owner, windowTitle, dialogTitle);
		//createComponents();
		pack();
	}
	
	protected void createComponents(){
		
		super.createComponents(cntTabbedPane);		
	}
	
	@Override
	void addButton(JButton button) {
		// TODO Auto-generated method stub		
	}

	@Override
	void removeButton(JButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void setIcon() {
		// TODO Auto-generated method stub
		
	}

}
