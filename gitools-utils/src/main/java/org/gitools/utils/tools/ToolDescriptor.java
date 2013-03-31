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

package org.gitools.utils.tools;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * It represents the descriptor for a tool
 * and contains all the relevant information for the tool.
 */

@XmlRootElement
public class ToolDescriptor {

	protected String name;
	protected String description;
	protected Class<?> argsClass;
	protected Class<? extends ToolLifeCycle> lifeCycleClass;
	
	public ToolDescriptor() {
	}
	
	public ToolDescriptor(
			String name, 
			String descr, 
			Class<?> argsObject, 
			Class<? extends ToolLifeCycle> toolClass) {
		
		this.name = name;
		this.description = descr;
		this.argsClass = argsObject;
		this.lifeCycleClass = toolClass;
	}

	@XmlElement
	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public Class<?> getArgumentsClass() {
		return argsClass;
	}
	
	protected void setArgumentsClass(Class<?> argsClass) {
		this.argsClass = argsClass;
	}
	
	@XmlElement
	public Class<? extends ToolLifeCycle> getLifeCycleClass() {
		return lifeCycleClass;
	}
	
	protected void setLifeCycleClass(Class<? extends ToolLifeCycle> lifeCycleClass) {
		this.lifeCycleClass = lifeCycleClass;
	}
}
