package org.gitools.ui.wizard.intogen.data;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class IntogenDataItemsPage extends AbstractWizardPage {

	private IntogenDataItemsPanel panel;

	public IntogenDataItemsPage() {
		setTitle("Select items type");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenDataItemsPanel();
		return panel;
	}

}
