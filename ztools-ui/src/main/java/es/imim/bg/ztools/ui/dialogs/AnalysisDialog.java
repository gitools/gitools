package es.imim.bg.ztools.ui.dialogs;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;


public abstract class AnalysisDialog extends BaseDialog {
		
	private static final long serialVersionUID = -3270597264001925514L;
	
	JTabbedPane cntTabbedPane;
	
	
	public AnalysisDialog(JFrame owner, String windowTitle, String dialogTitle, String dialogSubtitle) {
		
		super(owner, windowTitle, dialogTitle, dialogSubtitle);
		cntTabbedPane = new JTabbedPane();
	}
}
