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
public class AttributeDescription extends AbstractDescription {

	@XmlAttribute
	private String datasetLink;

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

}
