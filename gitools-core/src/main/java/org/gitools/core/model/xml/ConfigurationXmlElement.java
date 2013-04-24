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
package org.gitools.core.model.xml;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement(name = "configuration")
public class ConfigurationXmlElement {

    @XmlType(propOrder = {"key", "value"})
    @XmlRootElement
    public static class ConfigurationXmlEntry {
        @XmlAttribute
        private String key;

        @XmlAttribute
        private String value;

        public ConfigurationXmlEntry() {
        }

        public ConfigurationXmlEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getName() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    @NotNull
    @XmlElement(name = "property")
    private final List<ConfigurationXmlEntry> configuration = new ArrayList<ConfigurationXmlElement.ConfigurationXmlEntry>();

    public ConfigurationXmlElement() {
    }

    @NotNull
    public List<ConfigurationXmlEntry> getConfiguration() {
        return configuration;
    }
}
