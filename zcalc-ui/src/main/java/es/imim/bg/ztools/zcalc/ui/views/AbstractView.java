package es.imim.bg.ztools.zcalc.ui.views;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

	protected Icon icon;
	
	public Icon getIcon() {
		return icon;
	}
	
	public JPanel getPanel() {
		return this;
	}
}
