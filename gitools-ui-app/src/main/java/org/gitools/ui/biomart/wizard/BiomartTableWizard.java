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
package org.gitools.ui.biomart.wizard;

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.*;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * @noinspection ALL
 */
public class BiomartTableWizard extends AbstractWizard {

    private SaveFilePage saveFilePage;

    @Nullable
    private BiomartService biomartService;

    private DatasetConfig biomartConfig;

    private BiomartAttributeListPage attrListPage;

    private BiomartTableFilteringPage filteringPage;

    private BiomartFilterConfigurationPage filterListPage;

    private BiomartSourcePage sourcePage;

    @Nullable
    private MartLocation Database;

    @Nullable
    private DatasetInfo Dataset;

    public static final String FORMAT_PLAIN = "TSV";
    public static final String FORMAT_COMPRESSED_GZ = "GZ";

    @NotNull
    private final FileFormat[] supportedFormats = new FileFormat[]{new FileFormat("Tab Separated Fields", "tsv", true, false), new FileFormat("Tab Separated Fields compressed", "tsv.gz", true, false)};

    public BiomartTableWizard() { /*BiomartRestfulService biomartService /*IBiomartService biomartService*/

        setTitle("Import table ...");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_BIOMART_IMPORT, 96));
        setHelpContext("import_biomart");
    }

    @Override
    public void addPages() {

        // Source
        sourcePage = new BiomartSourcePage();
        addPage(sourcePage);

        // Attribute list
        attrListPage = new BiomartAttributeListPage();
        attrListPage.setTitle("Select attributes");
        addPage(attrListPage);

        // Advance filtering
        filterListPage = new BiomartFilterConfigurationPage();
        filterListPage.setTitle("Select filters");
        addPage(filterListPage);

        // Filtering
        filteringPage = new BiomartTableFilteringPage();
        addPage(filteringPage);

        // Destination
        saveFilePage = new SaveFilePage();
        saveFilePage.setTitle("Select destination file");
        saveFilePage.setFolder(Settings.getDefault().getLastDataPath());
        saveFilePage.setFormats(supportedFormats);
        addPage(saveFilePage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage = super.getNextPage(page);

        if (nextPage == attrListPage) {
            biomartService = sourcePage.getBiomartService();
            Database = sourcePage.getDataBase();
            Dataset = sourcePage.getDataset();

            attrListPage.setSource(biomartService, Database, Dataset);
        } else if (nextPage == filterListPage) {
            biomartConfig = attrListPage.getBiomartConfig();
            filterListPage.setSource(biomartService, biomartConfig);
        }

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
        return saveFilePage.getPathAsFile();
    }

    public List<AttributeDescription> getAttributeList() {
        return attrListPage.getAttributeList();
    }

    @NotNull
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
        //Add filters into dataset
        List<Filter> dsFilters = ds.getFilter();
        dsFilters.addAll(filterListPage.getFilters());

        Query query = new Query();
        query.setVirtualSchemaName(mart.getServerVirtualSchema());
        query.setHeader(1);
        query.setCount(0);
        query.setUniqueRows(1);
        query.getDatasets().add(ds);

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

    @Nullable
    MartLocation getDatabase() {
        return Database;
    }

    @Nullable
    DatasetInfo getDataset() {
        return Dataset;
    }

    @Nullable
    public BiomartService getService() {
        return biomartService;
    }
}
