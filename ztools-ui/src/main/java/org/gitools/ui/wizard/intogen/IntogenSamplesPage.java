package org.gitools.ui.wizard.intogen;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class IntogenSamplesPage extends AbstractWizardPage {

	private IntogenSamplesPanel panel;

	public IntogenSamplesPage() {
		setTitle("Select samples");
		setComplete(true); // TODO it will depend on number of samples selected
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenSamplesPanel();
		return panel;
	}

}
