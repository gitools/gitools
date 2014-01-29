/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.datasources.biomart.wizard;

import org.gitools.datasources.biomart.BiomartService;
import org.gitools.datasources.biomart.restful.model.Attribute;
import org.gitools.datasources.biomart.restful.model.Dataset;
import org.gitools.datasources.biomart.restful.model.DatasetConfig;
import org.gitools.datasources.biomart.restful.model.DatasetInfo;
import org.gitools.datasources.biomart.restful.model.Filter;
import org.gitools.datasources.biomart.restful.model.MartLocation;
import org.gitools.datasources.biomart.restful.model.Query;
import org.gitools.persistence.formats.FileFormat;
import org.gitools.persistence.formats.FileFormats;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.wizard.common.SaveFilePage;

import java.io.File;
import java.util.List;

public class BiomartModulesWizard extends AbstractWizard {

    private BiomartAttributePage modulesAttributePage;

    private BiomartAttributePage dataAttributePage;

    private SaveFilePage saveFilePage;

    private BiomartService biomartService;

    private DatasetConfig biomartConfig;

    private BiomartFilterConfigurationPage filterListPage;

    private BiomartSourcePage sourcePage;

    private MartLocation Database;

    private DatasetInfo Dataset;

    public static final String FORMAT_PLAIN = "TSV";
    public static final String FORMAT_COMPRESSED_GZ = "GZ";


    private final FileFormat[] supportedFormats = new FileFormat[]{new FileFormat(FileFormats.MODULES_2C_MAP.getTitle(), FileFormats.MODULES_2C_MAP.getExtension(), true, false), new FileFormat(FileFormats.MODULES_2C_MAP.getTitle() + " compressed", FileFormats.MODULES_2C_MAP.getExtension() + ".gz", true, false)};

    public BiomartModulesWizard() {/*BiomartRestfulService biomartService/*IBiomartService biomartService*/

        setTitle("Import modules...");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
        setHelpContext("import_biomart");
    }

    @Override
    public void addPages() {

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

        // Advanced filtering
        filterListPage = new BiomartFilterConfigurationPage();
        filterListPage.setTitle("Select filters");
        addPage(filterListPage);

        // Destination
        saveFilePage = new SaveFilePage();
        saveFilePage.setTitle("Select destination file");
        saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
        saveFilePage.setFormats(supportedFormats);
        addPage(saveFilePage);
    }

    @Override
    public void performFinish() {
        Settings.getDefault().setLastMapPath(saveFilePage.getFolder());
        Settings.getDefault().save();
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        if (page == sourcePage) {

            biomartService = sourcePage.getBiomartService();
            Database = sourcePage.getDataBase();
            Dataset = sourcePage.getDataset();

            modulesAttributePage.setSource(biomartService, Database, Dataset);

        } else if (page == modulesAttributePage) {

            dataAttributePage.setAttributePages(modulesAttributePage.getAttributePages());
        } else if (page == dataAttributePage) {
            //filterListPage.setSource(biomartService, Dataset);
            biomartConfig = modulesAttributePage.getBiomartConfig();
            filterListPage.setSource(biomartService, biomartConfig);
        }

        return super.getNextPage(page);
    }

    public File getSelectedFile() {
        return saveFilePage.getPathAsFile();
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

        //Add filters into dataset
        List<Filter> dsFilters = ds.getFilter();
        dsFilters.addAll(filterListPage.getFilters());

        Query query = new Query();
        query.setVirtualSchemaName(mart.getServerVirtualSchema());
        query.setHeader(header);
        query.setCount(count);
        query.setUniqueRows(uniqueRows);
        query.getDatasets().add(ds);

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

    MartLocation getDatabase() {
        return Database;
    }

    DatasetInfo getDataset() {
        return Dataset;
    }

    public BiomartService getService() {
        return biomartService;
    }
}
