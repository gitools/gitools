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
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @noinspection ALL
 */
public interface IWizard
{

    String getTitle();

    Icon getLogo();

    void addPages();

    IWizardPage getStartingPage();

    IWizardPage getCurrentPage();

    void setCurrentPage(IWizardPage currentPage);

    @Nullable
    IWizardPage getNextPage(IWizardPage page);

    IWizardPage getPreviousPage(IWizardPage page);

    boolean isLastPage(IWizardPage page);

    IWizardPage getPage(String name);

    IWizardPage[] getPages();

    int getPageCount();

    public void pageLeft(IWizardPage page);

    public void pageEntered(IWizardPage page);

    boolean canFinish();

    void performFinish();

    void performCancel();

    void addWizardUpdateListener(IWizardUpdateListener listener);

    void removeWizardUpdateListener(IWizardUpdateListener listener);

    public HelpContext getHelpContext();
}
