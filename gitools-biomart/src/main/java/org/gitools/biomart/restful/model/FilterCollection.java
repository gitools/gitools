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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.FIELD)

public class FilterCollection {
	
	@XmlElement
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
	private String enableSelectAll;

	@XmlElement(name = "FilterDescription")
	private List<FilterDescription> filterDescriptions;
	
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

	public String getEnableSelectAll() {
		return enableSelectAll;
	}

	public void setEnableSelectAll(String enableSelectAll) {
		this.enableSelectAll = enableSelectAll;
	}

	public List<FilterDescription> getFilterDescriptions() {
		return filterDescriptions;
	}

	public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
		this.filterDescriptions = filterDescriptions;
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

}
