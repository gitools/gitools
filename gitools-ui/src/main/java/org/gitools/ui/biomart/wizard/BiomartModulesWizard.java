package org.gitools.ui.biomart.wizard;

import java.io.File;
import org.biomart._80.martservicesoap.Attribute;
import org.biomart._80.martservicesoap.Dataset;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.Query;

/*import org.gitools.biomart.cxf.Attribute;
import org.gitools.biomart.cxf.Dataset;
import org.gitools.biomart.cxf.Mart;
import org.gitools.biomart.cxf.Query;
import org.gitools.biomart.IBiomartService;*/import org.gitools.biomart.BiomartCentralPortalService;


import org.gitools.persistence.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class BiomartModulesWizard extends AbstractWizard {

	private FileChooserPage destPathPage;
	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributePage modulesAttributePage;
	private BiomartAttributePage dataAttributePage;
	private SaveFilePage saveFilePage;

	//private IBiomartService biomartService;
	private BiomartCentralPortalService biomartService;

	public BiomartModulesWizard(BiomartCentralPortalService biomartService/*IBiomartService biomartService*/) {
		this.biomartService = biomartService;
		
		setTitle("Import modules...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
	}

	@Override
	public void addPages() {

		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
		saveFilePage.setFormats(biomartService.getSupportedFormats());
		addPage(saveFilePage);
		
		// Database
		databasePage = new BiomartDatabasePage(biomartService);
		addPage(databasePage);
		
		// Dataset
		datasetPage = new BiomartDatasetPage(biomartService);
		addPage(datasetPage);
		
		// Modules attribute
		modulesAttributePage = new BiomartAttributePage(biomartService);
		modulesAttributePage.setId(BiomartAttributePage.class.getCanonicalName() + "1");
		modulesAttributePage.setTitle("Select attribute for modules");
		addPage(modulesAttributePage);
		
		// Data attribute
		dataAttributePage = new BiomartAttributePage(biomartService);
		dataAttributePage.setId(BiomartAttributePage.class.getCanonicalName() + "2");
		dataAttributePage.setTitle("Select attribute for data");
		addPage(dataAttributePage);
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastMapPath(saveFilePage.getFolder());
		Settings.getDefault().save();
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == databasePage)
			datasetPage.setMart(databasePage.getMart());
		else if (page == datasetPage) {
			modulesAttributePage.setSource(
					databasePage.getMart(),
					datasetPage.getDataset());
		}
		else if (page == modulesAttributePage)
			dataAttributePage.setAttributePages(
					modulesAttributePage.getAttributePages());
		
		return super.getNextPage(page);
	}

	public File getSelectedFile() {
		return saveFilePage.getFile();
	}

	public Query getQuery() {
		Mart mart = databasePage.getMart();

		int header = 0;
		int count = 0;
		int uniqueRows = 1;

		Dataset ds = new Dataset();
		ds.setName(datasetPage.getDataset().getName());

		Attribute moduleAttr = new Attribute();
		moduleAttr.setName(modulesAttributePage.getAttribute().getName());

		Attribute dataAttr = new Attribute();
		dataAttr.setName(dataAttributePage.getAttribute().getName());

		ds.getAttribute().add(moduleAttr);
		ds.getAttribute().add(dataAttr);

		Query query = new Query();
		query.setVirtualSchemaName(mart.getServerVirtualSchema());
		query.setHeader(header);
		query.setCount(count);
		query.setUniqueRows(uniqueRows);
		query.getDataset().add(ds);

		/*try {
			List<ResultsRow> resultRows = port.query(
					mart.getServerVirtualSchema(), header, count, uniqueRows, Arrays.asList(ds));

			ListIterator<ResultsRow> it = resultRows.listIterator();
			while (it.hasNext()) {
				ResultsRow row = it.next();
				System.out.println(row.getItem());
			}

		} catch (BioMartException_Exception ex) {
			Logger.getLogger(BiomartModulesWizard.class.getName()).log(Level.SEVERE, null, ex);
		}*/

		return query;
	}

	public FileFormat getFormat() {
		return saveFilePage.getFormat();
	}
}
