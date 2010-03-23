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
package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class FilterDescription {

	@XmlAttribute
	private String field;

	@XmlAttribute
	private String value;

	@XmlAttribute
	private String tableConstraint;

	@XmlAttribute
	private String key;

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String qualifier;

	@XmlAttribute
	private String legal_qualifiers;

	@XmlAttribute
	private String buttonURL;

	@XmlAttribute
	private String regexp;

	@XmlAttribute
	private String defaultValue;

	@XmlAttribute
	private String defaultOn;

	@XmlAttribute
	private String filterList;

	@XmlAttribute
	private String setAttributePage;

	@XmlAttribute
	private String setAttribute;

	@XmlAttribute
	private String colForDisplay;

	@XmlAttribute
	private String pointerDataset;

	@XmlAttribute
	private String pointerInterface;

	@XmlAttribute
	private String pointerFilter;

	@XmlAttribute
	private String displayType;

	@XmlAttribute
	private String multipleValues;

	@XmlAttribute
	private String graph;

	@XmlAttribute
	private String style;

	@XmlAttribute
	private String autoCompletion;

	@XmlAttribute
	private String dependsOnType;

	@XmlAttribute
	private String dependsOn;

	@XmlAttribute
	private String checkForNulls;

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

	@XmlAttribute
	private String otherFilters;

	@XmlElement(name = "Option")
	private List<Option> options = new ArrayList<Option>();

	public String getAutoCompletion() {
		return autoCompletion;
	}

	public void setAutoCompletion(String autoCompletion) {
		this.autoCompletion = autoCompletion;
	}

	public String getButtonURL() {
		return buttonURL;
	}

	public void setButtonURL(String buttonURL) {
		this.buttonURL = buttonURL;
	}

	public String getCheckForNulls() {
		return checkForNulls;
	}

	public void setCheckForNulls(String checkForNulls) {
		this.checkForNulls = checkForNulls;
	}

	public String getColForDisplay() {
		return colForDisplay;
	}

	public void setColForDisplay(String colForDisplay) {
		this.colForDisplay = colForDisplay;
	}

	public String getDefaultOn() {
		return defaultOn;
	}

	public void setDefaultOn(String defaultOn) {
		this.defaultOn = defaultOn;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getDependsOnType() {
		return dependsOnType;
	}

	public void setDependsOnType(String dependsOnType) {
		this.dependsOnType = dependsOnType;
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

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
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

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLegal_qualifiers() {
		return legal_qualifiers;
	}

	public void setLegal_qualifiers(String legal_qualifiers) {
		this.legal_qualifiers = legal_qualifiers;
	}

	public String getMultipleValues() {
		return multipleValues;
	}

	public void setMultipleValues(String multipleValues) {
		this.multipleValues = multipleValues;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getOtherFilters() {
		return otherFilters;
	}

	public void setOtherFilters(String otherFilters) {
		this.otherFilters = otherFilters;
	}

	public String getPointerDataset() {
		return pointerDataset;
	}

	public void setPointerDataset(String pointerDataset) {
		this.pointerDataset = pointerDataset;
	}

	public String getPointerFilter() {
		return pointerFilter;
	}

	public void setPointerFilter(String pointerFilter) {
		this.pointerFilter = pointerFilter;
	}

	public String getPointerInterface() {
		return pointerInterface;
	}

	public void setPointerInterface(String pointerInterface) {
		this.pointerInterface = pointerInterface;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	public String getSetAttribute() {
		return setAttribute;
	}

	public void setSetAttribute(String setAttribute) {
		this.setAttribute = setAttribute;
	}

	public String getSetAttributePage() {
		return setAttributePage;
	}

	public void setSetAttributePage(String setAttributePage) {
		this.setAttributePage = setAttributePage;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTableConstraint() {
		return tableConstraint;
	}

	public void setTableConstraint(String tableConstraint) {
		this.tableConstraint = tableConstraint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
