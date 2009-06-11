package org.gitools.ui.wizard;

import javax.swing.JComponent;

public interface IWizardPage {

	Object getId();

	WizardDialog getWizard();

	void setWizard(WizardDialog wizard);

	boolean isPageComplete();

	JComponent createPageControls();

	boolean canFlipToNextPage();

}