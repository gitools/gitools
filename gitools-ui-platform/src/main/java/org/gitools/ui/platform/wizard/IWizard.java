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

import javax.swing.Icon;
import org.gitools.ui.platform.help.HelpContext;

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

	public void pageLeft(IWizardPage page);

	public void pageEntered(IWizardPage page);
	
	boolean canFinish();
	
	void performFinish();
	
	void performCancel();

	void addWizardUpdateListener(IWizardUpdateListener listener);

	void removeWizardUpdateListener(IWizardUpdateListener listener);

	public HelpContext getHelpContext();
}
