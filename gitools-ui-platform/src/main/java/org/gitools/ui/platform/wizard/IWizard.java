package org.gitools.ui.platform.wizard;

import javax.swing.Icon;

public interface IWizard {

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
	
	boolean canFinish();
	
	void performFinish();
	
	void performCancel();
	
	String getTitle();
	
	Icon getLogo();

	void addWizardUpdateListener(IWizardUpdateListener listener);

	void removeWizardUpdateListener(IWizardUpdateListener listener);
}
