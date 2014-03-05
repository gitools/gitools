/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.analysis;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolConfigurationXmlAdapter extends XmlAdapter<ToolConfigurationXmlElement, Map<String, String>> {


    @Override
    public Map<String, String> unmarshal(ToolConfigurationXmlElement v) throws Exception {
        Map<String, String> map = new HashMap<>();
        for (ToolConfigurationXmlElement.ConfigurationXmlEntry entry : v.getConfiguration())
            map.put(entry.getName(), entry.getValue());
        return map;
    }


    @Override
    public ToolConfigurationXmlElement marshal(Map<String, String> v) throws Exception {
        ToolConfigurationXmlElement e = new ToolConfigurationXmlElement();
        List<ToolConfigurationXmlElement.ConfigurationXmlEntry> conf = e.getConfiguration();
        for (Map.Entry<String, String> entry : v.entrySet())
            conf.add(new ToolConfigurationXmlElement.ConfigurationXmlEntry(entry.getKey(), entry.getValue()));
        return e;
    }
}
