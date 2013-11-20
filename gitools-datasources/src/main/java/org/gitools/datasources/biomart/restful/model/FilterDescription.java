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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterDescription extends AbstractDescription {

    @XmlAttribute
    private String displayType;

    @XmlAttribute
    private int multipleValues;

    @XmlAttribute
    private String type;

    @XmlAttribute
    private String qualifier;

    @XmlAttribute
    private String legal_qualifiers;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private String defaultValue;

    @XmlAttribute
    private String defaultOn;

    @XmlAttribute
    private String filterList;

    @XmlAttribute
    private String otherFilters;

    @XmlAttribute
    private String buttonURL;

    @XmlAttribute
    private String regexp;

    @XmlAttribute
    private String setAttributePage;

    @XmlAttribute
    private String setAttribute;

    @XmlAttribute
    private String colForDisplay;

    @XmlAttribute
    private String graph;

    @XmlAttribute
    private String style;

    @XmlAttribute
    private String dependsOnType;

    @XmlAttribute
    private String dependsOn;

    @XmlAttribute
    private String checkForNulls;

    @XmlAttribute
    private String autoCompletion;

    @XmlAttribute
    private String pointerDataset;

    @XmlAttribute
    private String pointerInterface;

    @XmlAttribute
    private String pointerFilter;

    @XmlElement(name = "Option")
    private List<Option> options = new ArrayList<Option>();

    FilterDescription() {
    }

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

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
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

    public String getLegal_qualifiers() {
        return legal_qualifiers;
    }

    public void setLegal_qualifiers(String legal_qualifiers) {
        this.legal_qualifiers = legal_qualifiers;
    }

    public int getMultipleValues() {
        return multipleValues;
    }

    public void setMultipleValues(int multipleValues) {
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
