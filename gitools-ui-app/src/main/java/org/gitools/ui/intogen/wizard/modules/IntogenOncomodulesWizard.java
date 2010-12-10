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

package org.gitools.ui.intogen.wizard.modules;

import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class IntogenOncomodulesWizard extends AbstractWizard {

	private IntogenOncomoduleSetPage dataSetPage;
	private IntogenOncomodulesListPage tumorTypePage;
	private IntogenOncomodulesListPage conditionPage;
	private IntogenOncomodulesListPage experimentsPage;

	public IntogenOncomodulesWizard() {
		super();
		
		setTitle("Import IntOGen cancer modules");
	}
	
	@Override
	public void addPages() {
		// Oncomodules Set
		dataSetPage = new IntogenOncomoduleSetPage();
		addPage(dataSetPage);
		
		experimentsPage = new IntogenOncomodulesListPage();
		experimentsPage.setTitle("Select experiments");
		experimentsPage.getListModel().addElement("Experiment 1");
		experimentsPage.getListModel().addElement("Experiment 2");
		addPage(experimentsPage);
		
		tumorTypePage = new IntogenOncomodulesListPage();
		tumorTypePage.setTitle("Select tumor types");
		tumorTypePage.getListModel().addElement("breast; any");
		tumorTypePage.getListModel().addElement("breast; adenocarcinoma, nos");
		addPage(tumorTypePage);
		
		conditionPage = new IntogenOncomodulesListPage();
		conditionPage.setTitle("Select conditions");
		conditionPage.getListModel().addElement("gain");
		conditionPage.getListModel().addElement("loss");
		addPage(conditionPage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == dataSetPage) {
			if (dataSetPage.isExperimentsSelected())
				return experimentsPage;
			else if (dataSetPage.isCombinationsSelected())
				return tumorTypePage;
		}
		return super.getNextPage(page);
	}

}
