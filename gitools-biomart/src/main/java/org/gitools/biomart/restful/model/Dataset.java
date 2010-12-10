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

package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class Dataset {

    @XmlAttribute(required = true)
    protected String name;

	@XmlAttribute(name = "interface")
	protected String _interface;

    @XmlElement(name = "Filter")
    protected List<Filter> filter = new ArrayList<Filter>(0);

	@XmlElement(name = "ValueFilter")
    protected List<Filter> valueFilter = new ArrayList<Filter>(0);

    @XmlElement(name = "Attribute", required = true)
    protected List<Attribute> attribute = new ArrayList<Attribute>(0);

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

	public String getInterface() {
		return _interface;
	}

	public void setInterface(String _interface) {
		this._interface = _interface;
	}

    public List<Filter> getFilter() {
        return this.filter;
    }

	public List<Filter> getValueFilter() {
		return valueFilter;
	}
	
    public List<Attribute> getAttribute() {
        return this.attribute;
    }
}
