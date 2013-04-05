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
package org.gitools.model;

import org.gitools.model.xml.ConfigurationXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * @noinspection ALL
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolConfig
{

    public static final String ENRICHMENT = "enrichment";
    public static final String ONCODRIVE = "oncodrive";

    private String name;

    @XmlJavaTypeAdapter(ConfigurationXmlAdapter.class)
    private Map<String, String> configuration = new HashMap<String, String>();

    public ToolConfig(String name)
    {
        this.name = name;
    }

    public ToolConfig()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, String> getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration)
    {
        this.configuration = configuration;
    }

    public void put(String name, String value)
    {
        configuration.put(name, value);
    }

    public String get(String name)
    {
        return configuration.get(name);
    }
}
