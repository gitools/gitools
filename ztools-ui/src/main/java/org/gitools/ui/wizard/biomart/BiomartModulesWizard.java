package org.gitools.ui.wizard.biomart;

import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;

public class BiomartModulesWizard extends AbstractWizard {

	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributePage modulesAttributePage;

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
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == databasePage)
			datasetPage.setMart(
					databasePage.getMart());
		else if (page == datasetPage) {
			final String datasetName = datasetPage.getDataset().getName();
			final String virtualSchema = databasePage.getMart().getServerVirtualSchema();
			modulesAttributePage.setSource(datasetName, virtualSchema);
		}
		
		return super.getNextPage(page);
	}

}
