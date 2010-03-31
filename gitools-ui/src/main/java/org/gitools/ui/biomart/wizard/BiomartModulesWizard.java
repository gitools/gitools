package org.gitools.ui.biomart.wizard;

import java.io.File;

import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.Query;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;


import org.gitools.persistence.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class BiomartModulesWizard extends AbstractWizard {

	private BiomartAttributePage modulesAttributePage;

	private BiomartAttributePage dataAttributePage;

	private SaveFilePage saveFilePage;

	private BiomartRestfulService biomartService;

	private BiomartFilterConfigurationPage filterListPage;

	private BiomartSourcePage sourcePage;

	private MartLocation Database;

	private DatasetInfo Dataset;
	
	public static final String FORMAT_TSV = "TSV";
	public static final String FORMAT_TSV_GZ = "GZ";

	private FileFormat[] supportedFormats = new FileFormat[] {
		new FileFormat("Tab Separated Fields", "tsv", FORMAT_TSV, true, false),
		new FileFormat("Tab Separated Fields GZip compressed", "tsv.gz", FORMAT_TSV_GZ, true, false)
	};
	
	public BiomartModulesWizard() {/*BiomartRestfulService biomartService/*IBiomartService biomartService*/

		setTitle("Import modules...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
	}

	@Override
	public void addPages() {

		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
		saveFilePage.setFormats(supportedFormats);
		addPage(saveFilePage);

		// Source
		sourcePage = new BiomartSourcePage();
		addPage(sourcePage);


		// FIXME Modules attribute
		modulesAttributePage = new BiomartAttributePage();
		modulesAttributePage.setId(BiomartAttributePage.class.getCanonicalName() + "1");
		modulesAttributePage.setTitle("Select attribute for items");
		addPage(modulesAttributePage);

		// FIXME Data attribute
		dataAttributePage = new BiomartAttributePage();
		dataAttributePage.setId(BiomartAttributePage.class.getCanonicalName() + "2");
		dataAttributePage.setTitle("Select attribute for modules");
		addPage(dataAttributePage);

		/*
		// Advance filtering
		filterListPage = new BiomartFilterConfigurationPage(biomartService);
		filterListPage.setTitle("Select filters");
		addPage(filterListPage);
		 * 
		 */
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastMapPath(saveFilePage.getFolder());
		Settings.getDefault().save();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page == sourcePage) {

			biomartService = sourcePage.getService();
			Database = sourcePage.getDataBase();
			Dataset = sourcePage.getDataset();

			modulesAttributePage.setSource(
					biomartService,
					Database,
					Dataset);
		} else if (page == modulesAttributePage) {

			dataAttributePage.setAttributePages(
					modulesAttributePage.getAttributePages());
		}

		/*else if (page == filterListPage)
		filterListPage.setSource(
		databasePage.getMart(),
		datasetPage.getDataset());
		 */
		return super.getNextPage(page);
	}

	public File getSelectedFile() {
		return saveFilePage.getFile();
	}

	public Query getQuery() {
		MartLocation mart = getDatabase();

		int header = 0;
		int count = 0;
		int uniqueRows = 1;

		Dataset ds = new Dataset();
		ds.setName(getDataset().getName());

		Attribute moduleAttr = new Attribute();
		moduleAttr.setName(modulesAttributePage.getAttribute().getInternalName());

		Attribute dataAttr = new Attribute();
		dataAttr.setName(dataAttributePage.getAttribute().getInternalName());

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

	public MartLocation getDatabase() {
		return Database;
	}

	public DatasetInfo getDataset() {
		return Dataset;
	}

	public BiomartRestfulService getService() {
		return biomartService;
	}
}
