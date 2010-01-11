package org.gitools.ui.wizard.intogen.data;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenDataResultsPage extends AbstractWizardPage {

	private IntogenDataResultsPanel panel;

	public IntogenDataResultsPage() {
		setTitle("Select results");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenDataResultsPanel();
		return panel;
	}

}
