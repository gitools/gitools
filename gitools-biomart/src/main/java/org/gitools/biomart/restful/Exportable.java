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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Exportable {

	@XmlAttribute
	private String linkName;

	@XmlAttribute
	private String linkVersion;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String attributes;

	@XmlAttribute
	private String orderBy;

	@XmlAttribute
	private String pointer;

	@XmlAttribute
	private String type;

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

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
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

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkVersion() {
		return linkVersion;
	}

	public void setLinkVersion(String linkVersion) {
		this.linkVersion = linkVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getPointer() {
		return pointer;
	}

	public void setPointer(String pointer) {
		this.pointer = pointer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
