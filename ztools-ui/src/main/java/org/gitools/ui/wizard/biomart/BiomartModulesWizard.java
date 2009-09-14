package org.gitools.ui.wizard.biomart;

import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;

public class BiomartModulesWizard extends AbstractWizard {

	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributePage modulesAttributePage;
	private BiomartAttributePage dataAttributePage;

	@Override
	public void addPages() {
		BioMartSoapService service = new BioMartSoapService();
		MartServiceSoap port = service.getBioMartSoapPort();
		
		databasePage = new BiomartDatabasePage(port);
		addPage(databasePage);
		
		datasetPage = new BiomartDatasetPage(port);
		addPage(datasetPage);
		
		modulesAttributePage = new BiomartAttributePage(port);
		modulesAttributePage.setTitle("Select attribute for modules");
		addPage(modulesAttributePage);
		
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
