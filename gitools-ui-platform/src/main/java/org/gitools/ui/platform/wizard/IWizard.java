package org.gitools.ui.platform.wizard;

import javax.swing.Icon;

public interface IWizard {

	String getTitle();

	Icon getLogo();

	void addPages();

	IWizardPage getStartingPage();
	
	IWizardPage getCurrentPage();
	
	void setCurrentPage(IWizardPage currentPage);
	
	IWizardPage getNextPage(IWizardPage page);
	
	IWizardPage getPreviousPage(IWizardPage page);
	
	boolean isLastPage(IWizardPage page);
	
	IWizardPage getPage(String name);
	
	IWizardPage[] getPages();
	
	int getPageCount();

	public void pageLeft(IWizardPage currentPage);

	public void pageEntered(IWizardPage page);
	
	boolean canFinish();
	
	void performFinish();
	
	void performCancel();

	void addWizardUpdateListener(IWizardUpdateListener listener);

	void removeWizardUpdateListener(IWizardUpdateListener listener);
}
