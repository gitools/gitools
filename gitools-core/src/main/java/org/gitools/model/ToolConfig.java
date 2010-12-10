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

package org.gitools.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.xml.ConfigurationXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolConfig {

	//FIXME This shouldn't be here
	public static final String ENRICHMENT = "enrichment";
	public static final String ONCODRIVE = "oncodrive";
	
	protected String name;

	@XmlJavaTypeAdapter(ConfigurationXmlAdapter.class)
	protected Map<String, String> configuration = new HashMap<String, String>();
	
	public ToolConfig(String name) {
		this.name = name;
	}
	
	public ToolConfig() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}
	
	public void put(String name, String value) {
		configuration.put(name, value);
	}
	
	public String get(String name) {
		return configuration.get(name);
	}
}
