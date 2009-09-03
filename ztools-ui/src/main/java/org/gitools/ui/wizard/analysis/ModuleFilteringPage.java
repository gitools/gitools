package org.gitools.ui.wizard.analysis;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class ModuleFilteringPage extends AbstractWizardPage {

	public ModuleFilteringPage() {
		setTitle("Select module filtering options");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		return new ModuleFilteringPanel();
	}

}
