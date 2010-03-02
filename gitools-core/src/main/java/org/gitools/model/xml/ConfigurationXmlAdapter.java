/*
 *  Copyright 2010 chris.
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

package org.gitools.model.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gitools.model.xml.ConfigurationXmlElement.ConfigurationXmlEntry;

public class ConfigurationXmlAdapter extends XmlAdapter<ConfigurationXmlElement, Map<String, String>> {

	@Override
	public Map<String, String> unmarshal(ConfigurationXmlElement v) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (ConfigurationXmlElement.ConfigurationXmlEntry entry : v.getConfiguration())
			map.put(entry.getName(), entry.getValue());
		return map;
	}

	@Override
	public ConfigurationXmlElement marshal(Map<String, String> v) throws Exception {
		ConfigurationXmlElement e = new ConfigurationXmlElement();
		List<ConfigurationXmlEntry> conf = e.getConfiguration();
		for (Map.Entry<String, String> entry : v.entrySet())
			conf.add(new ConfigurationXmlEntry(entry.getKey(), entry.getValue()));
		return e;
	}
}
