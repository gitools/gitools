package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.BaseAction;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.gitools.ui.platform.AppFrame;

public class UnimplementedAction extends BaseAction {

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

	private static final long serialVersionUID = -1820826246607830734L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(AppFrame.instance(),
				"That action is unimplemented.\n" +
				"Be patient my friend.");
	}

}
