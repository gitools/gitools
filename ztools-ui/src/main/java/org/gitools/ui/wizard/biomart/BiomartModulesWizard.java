package org.gitools.ui.wizard.biomart;

import javax.swing.JFileChooser;

import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;

public class BiomartModulesWizard extends AbstractWizard {

	private FileChooserPage destPathPage;
	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributePage modulesAttributePage;
	private BiomartAttributePage dataAttributePage;

	@Override
	public void addPages() {
		BioMartSoapService service = new BioMartSoapService();
		MartServiceSoap port = service.getBioMartSoapPort();
		
		// Destination directory
		
		destPathPage = new FileChooserPage();
		destPathPage.setTitle("Select destination folder");
		destPathPage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		addPage(destPathPage);
		
		// Database
		
		databasePage = new BiomartDatabasePage(port);
		addPage(databasePage);
		
		// Dataset
		
		datasetPage = new BiomartDatasetPage(port);
		addPage(datasetPage);
		
		// Modules attribute
		
		modulesAttributePage = new BiomartAttributePage(port);
		modulesAttributePage.setTitle("Select attribute for modules");
		addPage(modulesAttributePage);
		
		// Data attribute
		
		dataAttributePage = new BiomartAttributePage(port);
		dataAttributePage.setTitle("Select attribute for data");
		addPage(dataAttributePage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == databasePage)
			datasetPage.setMart(
					databasePage.getMart());
		else if (page == datasetPage)
			modulesAttributePage.setSource(
					databasePage.getMart(),
					datasetPage.getDataset());
		else if (page == modulesAttributePage)
			dataAttributePage.setAttributePages(
					modulesAttributePage.getAttributePages());
		
		return super.getNextPage(page);
	}

}
