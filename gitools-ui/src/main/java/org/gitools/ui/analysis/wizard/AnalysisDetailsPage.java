package org.gitools.ui.analysis.wizard;

import java.util.List;
import javax.swing.JComponent;
import org.gitools.model.Attribute;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class AnalysisDetailsPage extends AbstractWizardPage {

	private ArtifactDetailsPanel panel;

	public AnalysisDetailsPage() {
		setTitle("Analysis details");

		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ANALYSIS_DETAILS, 96));

		setComplete(true);

		panel = new ArtifactDetailsPanel();
	}
	
	@Override
	public JComponent createControls() {
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
