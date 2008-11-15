package es.imim.bg.ztools.ui.views;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

	private static final long serialVersionUID = -2379950551933668781L;

	protected Icon icon;
	
	public Icon getIcon() {
		return icon;
	}
	
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void refreshActions() {
	}
}
