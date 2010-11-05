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

@XmlAccessorType(XmlAccessType.FIELD)
public class Option extends FilterDescription {

	@XmlAttribute
	private String ref;

	@XmlAttribute
	private String isSelectable;

	@XmlElement(name = "PushAction")
	private List<PushAction> pushactions;

	public Option() {
	}

	public String getIsSelectable() {
		return isSelectable;
	}

	public void setIsSelectable(String isSelectable) {
		this.isSelectable = isSelectable;
	}

	public List<PushAction> getPushactions() {
		return pushactions;
	}

	public void setPushactions(List<PushAction> pushactions) {
		this.pushactions = pushactions;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
}
