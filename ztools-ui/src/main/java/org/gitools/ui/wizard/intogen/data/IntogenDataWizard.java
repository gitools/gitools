package org.gitools.ui.wizard.intogen.data;

import javax.swing.JFileChooser;

import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.wizard.intogen.IntogenColumnsPage;

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
		addPage(intogenDataResultsPage);
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
