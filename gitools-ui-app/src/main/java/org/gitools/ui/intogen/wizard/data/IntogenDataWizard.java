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

package org.gitools.ui.intogen.wizard.data;

import javax.swing.JFileChooser;

import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.intogen.wizard.IntogenColumnsPage;

public class IntogenDataWizard extends AbstractWizard {

	private FileChooserPage destPathPage;
	private IntogenDataSetPage intogenDataSetPage;
	private IntogenColumnsPage intogenSamplesPage;
	private IntogenDataItemsPage intogenDataItemsPage;
	private IntogenDataResultsPage intogenDataResultsPage;
	private IntogenColumnsPage intogenExperimentsPage;
	private IntogenColumnsPage intogenCombinationsPage;

	public IntogenDataWizard() {
		super();
		
		setTitle("Import IntOGen data");
	}
	
	@Override
	public void addPages() {
		// Destination directory
		
		destPathPage = new FileChooserPage();
		destPathPage.setTitle("Select destination folder");
		destPathPage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		addPage(destPathPage);

		// Data set
		
		intogenDataSetPage = new IntogenDataSetPage();
		addPage(intogenDataSetPage);
		
		// Data set > Samples : select samples
		
		intogenSamplesPage = new IntogenColumnsPage();
		intogenSamplesPage.setTitle("Select samples");
		addPage(intogenSamplesPage);
		
		// Data set > Experiments
		
		intogenExperimentsPage = new IntogenColumnsPage();
		intogenExperimentsPage.setTitle("Select experiments");
		addPage(intogenExperimentsPage);
		
		// Data set > Combinations
		
		intogenCombinationsPage = new IntogenColumnsPage();
		intogenCombinationsPage.setTitle("Select combinations");
		addPage(intogenCombinationsPage);
		
		// Items
		
		intogenDataItemsPage = new IntogenDataItemsPage();
		addPage(intogenDataItemsPage);
		
		// Results
		
		intogenDataResultsPage = new IntogenDataResultsPage();
		addPage((IWizardPage) intogenDataResultsPage); //FIXME this cast shouldn't be necessary
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == intogenDataSetPage) {
			if (intogenDataSetPage.isSamplesSelected())
				return intogenSamplesPage;
			else if (intogenDataSetPage.isExperimentsSelected())
				return intogenExperimentsPage;
			else if (intogenDataSetPage.isCombinationsSelected())
				return intogenCombinationsPage;
		}
		else if (page == intogenSamplesPage
				|| page == intogenExperimentsPage
				|| page == intogenCombinationsPage)
			return intogenDataItemsPage;
		
		return super.getNextPage(page);
	}

}
