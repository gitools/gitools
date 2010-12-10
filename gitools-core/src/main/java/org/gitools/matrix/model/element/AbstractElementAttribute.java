/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.matrix.model.element;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementProperty.class})
		
public abstract class AbstractElementAttribute 
		implements IElementAttribute, Serializable {

	private static final long serialVersionUID = -6293895024608178858L;

	protected String id;
	protected String name;
	protected String description;
	protected Class<?> valueClass;
	
	public AbstractElementAttribute() {
	}
	
	public AbstractElementAttribute(
			String id, String name, String description, 
			Class<?> valueClass) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.valueClass = valueClass;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		if (name != null)
			sb.append(" : ").append(name);
		if (valueClass != null)
			sb.append(" : ").append(valueClass.getSimpleName());
		if (description != null)
			sb.append(" : ").append(description);
		return sb.toString();
	}
}
