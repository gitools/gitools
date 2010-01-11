package org.gitools.ui.wizard.intogen;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenColumnsPage extends AbstractWizardPage {

	private IntogenColumnsPanel panel;

	public IntogenColumnsPage() {
		setTitle("Select columns");
		setComplete(true); // TODO it will depend on number of samples selected
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenColumnsPanel();
		return panel;
	}

}
