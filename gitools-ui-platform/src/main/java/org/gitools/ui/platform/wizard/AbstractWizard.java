/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import org.gitools.ui.platform.help.HelpContext;

public abstract class AbstractWizard implements IWizard, IWizardPageUpdateListener {

	private String title;
	
	private Icon logo;

	private HelpContext helpContext;
	
	protected IWizardPage currentPage;
	
	private List<IWizardPage> pages = new ArrayList<IWizardPage>();
	
	private Map<String, IWizardPage> pageIdMap = new HashMap<String, IWizardPage>();

	private List<IWizardUpdateListener> listeners = new ArrayList<IWizardUpdateListener>();
	
	public AbstractWizard() {
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
	public HelpContext getHelpContext() {
		return helpContext;
	}

	public void setHelpContext(HelpContext helpContext) {
		this.helpContext = helpContext;
	}

	public void setHelpContext(String helpContextId) {
		this.helpContext = new HelpContext(helpContextId);
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
	public void pageLeft(IWizardPage currentPage) {
	}

	@Override
	public void pageEntered(IWizardPage page) {
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
