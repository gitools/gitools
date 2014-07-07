/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.platform.wizard;

import org.gitools.ui.platform.help.HelpContext;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWizard implements IWizard, IWizardPageUpdateListener {

    protected IWizardPage currentPage;
    private String title;
    private Icon logo;
    private HelpContext helpContext;
    private List<IWizardPage> pages;
    private Map<String, IWizardPage> pageIdMap;
    private List<IWizardUpdateListener> listeners;

    protected AbstractWizard() {
        init();
    }

    protected void init() {
        pages = new ArrayList<>();
        pageIdMap = new HashMap<>();
        listeners = new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        pageUpdated(null);
    }

    @Override
    public Icon getLogo() {
        return logo;
    }

    protected void setLogo(Icon icon) {
        this.logo = icon;
        pageUpdated(null);
    }

    @Override
    public HelpContext getHelpContext() {
        return helpContext;
    }

    protected void setHelpContext(String helpContextId) {
        this.helpContext = new HelpContext(helpContextId);
    }

    public void setHelpContext(HelpContext helpContext) {
        this.helpContext = helpContext;
    }

    protected void addPage(IWizardPage page) {
        String id = page.getId();
        if (id == null) {
            id = "Page" + pages.size();
        }

        addPage(id, page);
    }

    protected void addPage(String id, IWizardPage page) {
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
    }

    @Override
    public void performCancel() {
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

    public void fireWizardUpdate() {
        for (IWizardUpdateListener l : listeners)
            l.wizardUpdated(this);
    }
}
