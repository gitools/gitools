package org.gitools.ui.wizard.analysis;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class StatisticalTestPage extends AbstractWizardPage {

	public StatisticalTestPage() {
		setTitle("Select statistical test");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		return new StatisticalTestPanel();
	}

}
