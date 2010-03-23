/*
 *  Copyright 2010 xavier.
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
package org.gitools.biomart.restful;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(name = "DatasetConfig")
public class DatasetConfig {
	@XmlAttribute
	private String dataset;

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String visible;

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

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getVisibleFilterPage() {
		return visibleFilterPage;
	}

	public void setVisibleFilterPage(String visibleFilterPage) {
		this.visibleFilterPage = visibleFilterPage;
	}


}
