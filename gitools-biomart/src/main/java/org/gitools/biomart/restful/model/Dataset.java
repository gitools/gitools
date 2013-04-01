/*
 * #%L
 * gitools-biomart
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
package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class Dataset
{

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

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public String getInterface()
    {
        return _interface;
    }

    public void setInterface(String _interface)
    {
        this._interface = _interface;
    }

    public List<Filter> getFilter()
    {
        return this.filter;
    }

    public List<Filter> getValueFilter()
    {
        return valueFilter;
    }

    public List<Attribute> getAttribute()
    {
        return this.attribute;
    }
}
