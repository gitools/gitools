/*
 * #%L
 * gitools-biomart
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
package org.gitools.datasources.biomart.restful.model;

import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement(name = "DatasetConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatasetConfig {

    @XmlAttribute
    private String dataset;

    @XmlAttribute
    private String type;

    @XmlAttribute
    private int visible;

    @XmlAttribute
    private String visibleFilterPage;

    @XmlAttribute
    private String version;

    @XmlAttribute
    private String optional_parameters;

    @XmlAttribute
    private String defaultDataset;

    @XmlAttribute
    private String datasetID;

    @XmlAttribute
    private String modified;

    @XmlAttribute
    private String martUsers;

    @XmlAttribute
    private String interfaces;

    @XmlAttribute
    private String primaryKeyRestriction;

    @XmlAttribute
    private String template;

    @XmlAttribute
    private String softwareVersion;

    @XmlAttribute
    private String noCount;

    @XmlAttribute
    private String entryLabel;

    @XmlAttribute
    private String splitNameUsing;

    @XmlAttribute
    private String internalName;

    @XmlAttribute
    private String displayName;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private String hidden;

    @XmlAttribute
    private String hideDisplay;

    @XmlElement(name = "MainTable")
    private List<String> mainTables = new ArrayList<String>();

    @XmlElement(name = "Key")
    private List<String> keys = new ArrayList<String>();

    @XmlElement(name = "Importable")
    private List<Importable> importables = new ArrayList<Importable>();

    @XmlElement(name = "Exportable")
    private List<Exportable> exportables = new ArrayList<Exportable>();

    @XmlElement(name = "FilterPage")
    private List<FilterPage> filterPages = new ArrayList<FilterPage>();

    @XmlElement(name = "AttributePage")
    private List<AttributePage> attributePages = new ArrayList<AttributePage>();


    public static DatasetConfig load(Reader reader) {
        DatasetConfig config = null;
        try {
            JAXBContext context = JAXBContext.newInstance(DatasetConfig.class);
            Unmarshaller u = context.createUnmarshaller();
            config = (DatasetConfig) u.unmarshal(reader);
        } catch (JAXBException ex) {
            LoggerFactory.getLogger(DatasetConfig.class).error("Error loading DatasetConfig", ex);
        }
        return config;
    }

    public DatasetConfig() {
    }

    public List<AttributePage> getAttributePages() {
        return attributePages;
    }

    public void setAttributePages(List<AttributePage> AttributePages) {
        this.attributePages = AttributePages;
    }

    public List<Exportable> getExportables() {
        return exportables;
    }

    public void setExportables(List<Exportable> Exportables) {
        this.exportables = Exportables;
    }

    public List<FilterPage> getFilterPages() {
        return filterPages;
    }

    public void setFilterPages(List<FilterPage> FilterPages) {
        this.filterPages = FilterPages;
    }

    public List<Importable> getImportables() {
        return importables;
    }

    public void setImportables(List<Importable> Importables) {
        this.importables = Importables;
    }

    public List<String> getKey() {
        return keys;
    }

    public void setKey(List<String> Key) {
        this.keys = Key;
    }

    public List<String> getMainTable() {
        return mainTables;
    }

    public void setMainTable(List<String> MainTable) {
        this.mainTables = MainTable;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(String datasetID) {
        this.datasetID = datasetID;
    }

    public String getDefaultDataset() {
        return defaultDataset;
    }

    public void setDefaultDataset(String defaultDataset) {
        this.defaultDataset = defaultDataset;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEntryLabel() {
        return entryLabel;
    }

    public void setEntryLabel(String entryLabel) {
        this.entryLabel = entryLabel;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String getHideDisplay() {
        return hideDisplay;
    }

    public void setHideDisplay(String hideDisplay) {
        this.hideDisplay = hideDisplay;
    }

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getMartUsers() {
        return martUsers;
    }

    public void setMartUsers(String martUsers) {
        this.martUsers = martUsers;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getNoCount() {
        return noCount;
    }

    public void setNoCount(String noCount) {
        this.noCount = noCount;
    }

    public String getOptional_parameters() {
        return optional_parameters;
    }

    public void setOptional_parameters(String optional_parameters) {
        this.optional_parameters = optional_parameters;
    }

    public String getPrimaryKeyRestriction() {
        return primaryKeyRestriction;
    }

    public void setPrimaryKeyRestriction(String primaryKeyRestriction) {
        this.primaryKeyRestriction = primaryKeyRestriction;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getSplitNameUsing() {
        return splitNameUsing;
    }

    public void setSplitNameUsing(String splitNameUsing) {
        this.splitNameUsing = splitNameUsing;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getType() {
        return type;
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getVisibleFilterPage() {
        return visibleFilterPage;
    }

    public void setVisibleFilterPage(String visibleFilterPage) {
        this.visibleFilterPage = visibleFilterPage;
    }
}
