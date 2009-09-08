package org.gitools.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.gitools.ui.AppFrame;

public class UnimplementedAction extends BaseAction {

	public UnimplementedAction() {
		super("unimplemented");
	}
	
	public UnimplementedAction(String name) {
		super(name);
		setDefaultEnabled(true);
	}

	private static final long serialVersionUID = -1820826246607830734L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(AppFrame.instance(),
				"That action is unimplemented.\n" +
				"Be patient my friend.");
	}

}
