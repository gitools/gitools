package org.gitools.ui.wizard.intogen.data;

import javax.swing.JFileChooser;

import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.wizard.intogen.IntogenSamplesPage;

public class IntogenDataWizard extends AbstractWizard {

	private FileChooserPage destPathPage;
	private IntogenDataSetPage intogenDataSetPage;
	private IntogenSamplesPage intogenSamplesPage;
	private IntogenDataItemsPage intogenDataItemsPage;
	private IntogenDataResultsPage intogenDataResultsPage;

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
		
		intogenSamplesPage = new IntogenSamplesPage();
		addPage(intogenSamplesPage);
		
		// Data set > Experiments
		// ...
		
		// Data set > Combinations
		// ...
		
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
				return null;
			else if (intogenDataSetPage.isCombinationsSelected())
				return null;
		}
		
		else if (page == intogenSamplesPage)
			return intogenDataItemsPage;
		
		return super.getNextPage(page);
	}

}
