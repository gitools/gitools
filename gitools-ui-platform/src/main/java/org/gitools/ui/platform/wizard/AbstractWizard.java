package org.gitools.ui.platform.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

public abstract class AbstractWizard implements IWizard, IWizardPageUpdateListener {

	private String title;
	
	private Icon logo;
	
	protected IWizardPage currentPage;
	
	private List<IWizardPage> pages = new ArrayList<IWizardPage>();
	
	private Map<String, IWizardPage> pageIdMap = new HashMap<String, IWizardPage>();

	private List<IWizardUpdateListener> listeners = new ArrayList<IWizardUpdateListener>();
	
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

		page.addPageUpdateListener(this);
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
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		fireWizardUpdate();
	}
	
	@Override
	public Icon getLogo() {
		return logo;
	}
	
	public void setLogo(Icon icon) {
		this.logo = icon;
		fireWizardUpdate();
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

	@Override
	public void addWizardUpdateListener(IWizardUpdateListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeWizardUpdateListener(IWizardUpdateListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void pageUpdated(IWizardPage page) {
		for (IWizardUpdateListener l : listeners)
			l.pageUpdated(page);
	}

	private void fireWizardUpdate() {
		for (IWizardUpdateListener l : listeners)
			l.wizardUpdated(this);
	}
}
