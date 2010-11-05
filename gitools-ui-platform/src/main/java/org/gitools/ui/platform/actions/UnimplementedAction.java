package org.gitools.ui.platform.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class UnimplementedAction extends BaseAction {

	private static final long serialVersionUID = -1820826246607830734L;

	public UnimplementedAction() {
		super("unimplemented");
	}
	
	public UnimplementedAction(String name) {
		super(name);
		setDefaultEnabled(true);
	}
	
	public UnimplementedAction(String name, boolean enabled) {
		super(name);
		setDefaultEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(getParent(),
				"That action is unimplemented.\n" +
				"Be patient my friend.");
	}

	protected Window getParent() {
		return null;
	}
}
