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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


@XmlAccessorType(XmlAccessType.FIELD)

public class AttributePage {

	@XmlAttribute
	private String outFormats;

	@XmlAttribute
	private String maxSelect;

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

	@XmlElement(name = "AttributeGroup")
	private List<AttributeGroup> attributeGroups;

	public List<AttributeGroup> getAttributeGroups() {
		return attributeGroups;
	}

	public void setAttributeGroups(List<AttributeGroup> attributeGroups) {
		this.attributeGroups = attributeGroups;
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

	public String getMaxSelect() {
		return maxSelect;
	}

	public void setMaxSelect(String maxSelect) {
		this.maxSelect = maxSelect;
	}

	public String getOutFormats() {
		return outFormats;
	}

	public void setOutFormats(String outFormats) {
		this.outFormats = outFormats;
	}

}
