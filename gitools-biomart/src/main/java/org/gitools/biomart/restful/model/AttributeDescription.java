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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)

public class AttributeDescription {

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
	private String datasetLink;

	@XmlAttribute
	private String field;

	@XmlAttribute
	private String tableConstraint;

	@XmlAttribute
	private String key;

	@XmlAttribute
	private String source;

	@XmlAttribute
	private String homepageURL;

	@XmlAttribute
	private String linkoutURL;

	@XmlAttribute
	private String imageURL;

	@XmlAttribute
	private String maxLength;

	@XmlAttribute
	private String pointerDataset;

	@XmlAttribute
	private String pointerInterface;

	@XmlAttribute
	private String pointerAttribute;

	@XmlAttribute
	private String pointerFilter;

	@XmlAttribute
	private String checkForNulls;

	@XmlAttribute
	private String pipeDisplay;


	public String getCheckForNulls() {
		return checkForNulls;
	}

	public void setCheckForNulls(String checkForNulls) {
		this.checkForNulls = checkForNulls;
	}

	public String getDatasetLink() {
		return datasetLink;
	}

	public void setDatasetLink(String datasetLink) {
		this.datasetLink = datasetLink;
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

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
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

	public String getHomepageURL() {
		return homepageURL;
	}

	public void setHomepageURL(String homepageURL) {
		this.homepageURL = homepageURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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

	public String getLinkoutURL() {
		return linkoutURL;
	}

	public void setLinkoutURL(String linkoutURL) {
		this.linkoutURL = linkoutURL;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getPipeDisplay() {
		return pipeDisplay;
	}

	public void setPipeDisplay(String pipeDisplay) {
		this.pipeDisplay = pipeDisplay;
	}

	public String getPointerAttribute() {
		return pointerAttribute;
	}

	public void setPointerAttribute(String pointerAttribute) {
		this.pointerAttribute = pointerAttribute;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTableConstraint() {
		return tableConstraint;
	}

	public void setTableConstraint(String tableConstraint) {
		this.tableConstraint = tableConstraint;
	}

}
