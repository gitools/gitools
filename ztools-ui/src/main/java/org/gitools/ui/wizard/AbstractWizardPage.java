package org.gitools.ui.wizard;

import javax.swing.JComponent;

public abstract class AbstractWizardPage implements IWizardPage {

	private static final long serialVersionUID = -4330234851091328953L;

	private String id;
	
	private IWizard wizard;
	
	private boolean pageComplete;
	
	private String title;
	
	private String message;
	
	public AbstractWizardPage() {
	}
	
	public AbstractWizardPage(String id) {
		this.id = id;
		this.pageComplete = false;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public IWizard getWizard() {
		return wizard;
	}
	
	@Override
	public void setWizard(IWizard wizard) {
		this.wizard = wizard;
	}
	
	@Override
	public boolean isComplete() {
		return pageComplete;
	}
	
	public void setComplete(boolean complete) {
		this.pageComplete = complete;
		updateDialog();
	}
	
	public abstract JComponent createControls();
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		updateDialog();
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
		updateDialog();
	}
	
	protected void updateDialog() {
		IWizard wizard = getWizard();
		if (wizard == null)
			return;
		
		WizardDialog dialog = wizard.getDialog();
		if (dialog == null)
			return;
		
		dialog.updateState();
	}
}
