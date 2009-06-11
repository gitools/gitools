package org.gitools.ui.wizard;

import javax.swing.JComponent;

public abstract class WizardPage implements IWizardPage {

	private static final long serialVersionUID = -4330234851091328953L;

	private Object id;
	
	private WizardDialog wizard;
	
	private boolean pageComplete;
	
	public WizardPage(Object id) {
		this.id = id;
		this.pageComplete = false;
	}
	
	public Object getId() {
		return id;
	}
	
	public WizardDialog getWizard() {
		return wizard;
	}
	
	public void setWizard(WizardDialog wizard) {
		this.wizard = wizard;
	}
	
	public boolean isPageComplete() {
		return pageComplete;
	}
	
	public void setPageComplete(boolean complete) {
		this.pageComplete = complete;
	}
	
	public abstract JComponent createPageControls();
	
	public abstract boolean canFlipToNextPage();
}
