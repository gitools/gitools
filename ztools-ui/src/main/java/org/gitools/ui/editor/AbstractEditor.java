package org.gitools.ui.editor;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class AbstractEditor extends JPanel implements IEditor {

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
