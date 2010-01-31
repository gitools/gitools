package org.gitools.ui.analysis.htest.wizard;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

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

	public String getAnalysisTitle() {
		return panel.titleField.getText();
	}
	
	public String getAnalysisNotes() {
		return panel.notesArea.getText();
	}
}
