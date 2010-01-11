package org.gitools.ui.wizard.common;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class NameFilterPage extends AbstractWizardPage {

	private NameFilterPanel panel;

	public NameFilterPage() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JComponent createControls() {
		panel = new NameFilterPanel();
		return panel;
	}

}
