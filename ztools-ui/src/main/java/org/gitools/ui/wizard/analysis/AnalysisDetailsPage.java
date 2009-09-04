package org.gitools.ui.wizard.analysis;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class AnalysisDetailsPage extends AbstractWizardPage {

	private AnalysisDetailsPanel panel;

	public AnalysisDetailsPage() {
		setTitle("Analysis details");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new AnalysisDetailsPanel();
		
		return panel;
	}

}
