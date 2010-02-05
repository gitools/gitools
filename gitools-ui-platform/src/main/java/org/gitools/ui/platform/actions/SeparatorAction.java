package org.gitools.ui.platform.actions;

import java.awt.event.ActionEvent;

public final class SeparatorAction extends BaseAction {

	private static final long serialVersionUID = -6830893427407652450L;

	public SeparatorAction() {
		super(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public boolean isSeparator() {
		return true;
	}
}
