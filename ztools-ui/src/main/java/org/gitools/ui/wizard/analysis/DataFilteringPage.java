package org.gitools.ui.wizard.analysis;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class DataFilteringPage extends AbstractWizardPage {

	public DataFilteringPage() {
		setTitle("Select data filtering options");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		return new DataFilteringPanel();
	}

}
