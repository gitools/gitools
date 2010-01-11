package org.gitools.ui.platform.wizard;

import javax.swing.JComponent;

public interface IWizardPage {

	String getId();
	
	void setId(String id);

	IWizard getWizard();
	
	void setWizard(IWizard wizard);

	boolean isComplete();

	JComponent createControls();
	
	void updateControls();

	String getTitle();

	String getMessage();
}