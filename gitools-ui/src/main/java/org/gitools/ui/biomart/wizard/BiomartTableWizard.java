/*
 *  Copyright 2009 chris.
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
package org.gitools.ui.biomart.wizard;

import java.io.File;
import java.util.List;
import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.Query;

import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;

import org.gitools.persistence.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class BiomartTableWizard extends AbstractWizard {

	private SaveFilePage saveFilePage;

	private BiomartRestfulService biomartService;

	private BiomartAttributeListPage attrListPage;

	private BiomartTableFilteringPage filteringPage;

	private BiomartFilterConfigurationPage filterListPage;

	private BiomartSourcePage sourcePage;

	private MartLocation Database;

	private DatasetInfo Dataset;

	public static final String FORMAT_PLAIN = "TSV";
	public static final String FORMAT_COMPRESSED_GZ = "GZ";

	private FileFormat[] supportedFormats = new FileFormat[] {
		new FileFormat("Tab Separated Fields", "tsv", FORMAT_PLAIN, true, false),
		new FileFormat("Tab Separated Fields compressed", "tsv.gz", FORMAT_COMPRESSED_GZ, true, false)
	};

	public BiomartTableWizard() { /*BiomartRestfulService biomartService /*IBiomartService biomartService*/

		setTitle("Import table ...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
	}

	@Override
	public void addPages() {

		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastDataPath());
		saveFilePage.setFormats(supportedFormats);
		addPage(saveFilePage);

		// Source
		sourcePage = new BiomartSourcePage();
		addPage(sourcePage);

		// Attribute list
		attrListPage = new BiomartAttributeListPage();
		attrListPage.setTitle("Select attributes");
		addPage(attrListPage);

		/*
		// Advance filtering
		filterListPage = new BiomartFilterConfigurationPage(biomartService);
		filterListPage.setTitle("Select filters");
		addPage(filterListPage);
		 */

		// Filtering
		filteringPage = new BiomartTableFilteringPage();
		addPage(filteringPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = super.getNextPage(page);

		if (nextPage == attrListPage) {

			biomartService = sourcePage.getService();
			Database = sourcePage.getDataBase();
			Dataset = sourcePage.getDataset();

			attrListPage.setSource(
					biomartService,
					Database,
					Dataset);
		}

		/*
		else if (nextPage == filterListPage) {
		filterListPage.setSource(
		databasePage.getMart(),
		datasetPage.getDataset());
		}
		 */
		return nextPage;
	}

	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page == attrListPage && page.isComplete();

		return canFinish;
	}

	@Override
	public void performFinish() {
		super.performFinish();

		Settings.getDefault().setLastDataPath(saveFilePage.getFolder());
		Settings.getDefault().save();
	}

	public File getSelectedFile() {
		return saveFilePage.getFile();
	}

	public List<AttributeDescription> getAttributeList() {
		return attrListPage.getAttributeList();
	}

	public Query getQuery() {
		MartLocation mart = getDatabase();

		Dataset ds = new Dataset();
		ds.setName(getDataset().getName());

		List<Attribute> dsattrs = ds.getAttribute();
		for (AttributeDescription attrInfo : attrListPage.getAttributeList()) {
			Attribute attr = new Attribute();
			attr.setName(attrInfo.getInternalName());
			dsattrs.add(attr);
		}

		Query query = new Query();
		query.setVirtualSchemaName(mart.getServerVirtualSchema());
		query.setHeader(1);
		query.setCount(0);
		query.setUniqueRows(1);
		query.getDataset().add(ds);

		return query;
	}

	public FileFormat getFormat() {
		return saveFilePage.getFormat();
	}

	public boolean isSkipRowsWithEmptyValuesEnabled() {
		return filteringPage.isSkipRowsWithEmptyValuesEnabled();
	}

	public String emptyValuesReplacement() {
		return filteringPage.emptyValuesReplacement();
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
