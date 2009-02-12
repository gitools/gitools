package es.imim.bg.ztools.ui.dialogs;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import es.imim.bg.ztools.ui.views.WelcomeView;

@Deprecated
public class ZCalcAnalysisDialog extends AnalysisDialog {

	private static final long serialVersionUID = -5440110772686552307L;

	public ZCalcAnalysisDialog(JFrame owner, String windowTitle, String dialogTitle) {
		this(owner, windowTitle, dialogTitle, null);
	}
	
	public ZCalcAnalysisDialog(JFrame owner, String windowTitle, String dialogTitle, String dialogSubtitle) {
		super(owner, windowTitle, dialogTitle, dialogSubtitle);
		createComponents();
		pack();
	}
	
	protected void createComponents(){
				
		cntTabbedPane.addTab("tab1", new WelcomeView());
		cntTabbedPane.addTab("tab2", new JButton("asdf"));
		cntTabbedPane.addTab("tab3", new JTextArea("asdf"));
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
