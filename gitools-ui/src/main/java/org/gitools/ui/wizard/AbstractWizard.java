package org.gitools.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

public abstract class AbstractWizard implements IWizard {

	private WizardDialog dialog;
	
	private String title;
	
	private Icon icon;
	
	protected IWizardPage currentPage;
	
	private List<IWizardPage> pages = new ArrayList<IWizardPage>();
	
	private Map<String, IWizardPage> pageIdMap = new HashMap<String, IWizardPage>();
	
	public AbstractWizard() {
	}
	
	public void addPage(IWizardPage page) {
		String id = page.getId();
		if (id == null)
			id = "Page" + pages.size();
		
		addPage(id, page);
	}
	
	public void addPage(String id, IWizardPage page) {
		page.setId(id);
		page.setWizard(this);
		pages.add(page);
		pageIdMap.put(id, page);
	}
	
	@Override
	public IWizardPage getStartingPage() {
		return pages.size() > 0 ? pages.get(0) : null;
	}
	
	@Override
	public IWizardPage getCurrentPage() {
		return currentPage;
	}
	
	@Override
	public void setCurrentPage(IWizardPage currentPage) {
		this.currentPage = currentPage;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		int idx = pages.indexOf(page);
		
		return idx == -1 || idx == pages.size() - 1 ? null : pages.get(idx + 1);
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		int idx = pages.indexOf(page);
		
		return idx == -1 || idx == 0 ? null : pages.get(idx - 1);
	}
	
	@Override
	public boolean isLastPage(IWizardPage page) {
		return page.equals(pages.get(pages.size() - 1));
	}
	
	@Override
	public IWizardPage getPage(String id) {
		return pageIdMap.get(id);
	}
	
	@Override
	public IWizardPage[] getPages() {
		final IWizardPage[] pagesArray = new IWizardPage[pages.size()];
		pages.toArray(pagesArray);
		return pagesArray;
	}
	
	@Override
	public int getPageCount() {
		return pages.size();
	}
	
	@Override
	public WizardDialog getDialog() {
		return dialog;
	}
	
	@Override
	public void setDialog(WizardDialog dialog) {
		this.dialog = dialog;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	@Override
	public boolean canFinish() {
		return currentPage != null ? currentPage.isComplete() && isLastPage(currentPage) : false;
	}
	
	@Override
	public void performFinish() {
		// do nothing
	}
	
	@Override
	public void performCancel() {
		// do nothing
	}
}
