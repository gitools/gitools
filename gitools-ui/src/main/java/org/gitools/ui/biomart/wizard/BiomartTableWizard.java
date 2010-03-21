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
import org.biomart._80.martservicesoap.Attribute;
/*import org.gitools.biomart.cxf.Attribute;
import org.gitools.biomart.cxf.AttributeInfo;
import org.gitools.biomart.cxf.Dataset;
import org.gitools.biomart.cxf.Mart;
import org.gitools.biomart.cxf.Query;
import org.gitools.biomart.IBiomartService;*/import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.Dataset;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.Query;

import org.gitools.biomart.BiomartCentralPortalService;

import org.gitools.persistence.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class BiomartTableWizard extends AbstractWizard {

	private SaveFilePage saveFilePage;
	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributeListPage attrListPage;
	private BiomartTableFilteringPage filteringPage;

	//private IBiomartService biomartService;
	private BiomartCentralPortalService biomartService;

	public BiomartTableWizard(BiomartCentralPortalService biomartService /*IBiomartService biomartService*/) {
		this.biomartService = biomartService;
		
		setTitle("Import table ...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
	}
	
	@Override
	public void addPages() {
		
		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastDataPath());
		saveFilePage.setFormats(biomartService.getSupportedFormats());
		addPage(saveFilePage);
		
		// Database
		databasePage = new BiomartDatabasePage(biomartService);
		addPage(databasePage);

		// Dataset
		datasetPage = new BiomartDatasetPage(biomartService);
		addPage(datasetPage);

		// Attribute list
		attrListPage = new BiomartAttributeListPage(biomartService);
		attrListPage.setTitle("Select attributes");
		addPage(attrListPage);

		// Filtering
		filteringPage = new BiomartTableFilteringPage();
		addPage(filteringPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = super.getNextPage(page);

		if (nextPage == datasetPage)
			datasetPage.setMart(databasePage.getMart());
		else if (nextPage == attrListPage)
			attrListPage.setSource(
					databasePage.getMart(),
					datasetPage.getDataset());
	
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

	public List<AttributeInfo> getAttributeList() {
		return attrListPage.getAttributeList();
	}

	public Query getQuery() {
		Mart mart = databasePage.getMart();

		Dataset ds = new Dataset();
		ds.setName(datasetPage.getDataset().getName());

		List<Attribute> dsattrs = ds.getAttribute();
		for (AttributeInfo attrInfo : attrListPage.getAttributeList()) {
			Attribute attr = new Attribute();
			attr.setName(attrInfo.getName());
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
}
