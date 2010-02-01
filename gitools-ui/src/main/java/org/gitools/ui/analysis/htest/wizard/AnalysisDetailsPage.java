package org.gitools.ui.analysis.htest.wizard;

import org.gitools.ui.panels.ArtifactDetailsPanel;
import java.util.List;
import javax.swing.JComponent;
import org.gitools.model.Attribute;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class AnalysisDetailsPage extends AbstractWizardPage {

	private ArtifactDetailsPanel panel;

	public AnalysisDetailsPage() {
		setTitle("Analysis details");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new ArtifactDetailsPanel();
		
		return panel;
	}

	public String getAnalysisTitle() {
		return panel.getArtifactTitle();
	}
	
	public String getAnalysisNotes() {
		return panel.getArtifactNotes();
	}

	public List<Attribute> getAnalysisAttributes() {
		return panel.getArtifactAttributes();
	}
}
