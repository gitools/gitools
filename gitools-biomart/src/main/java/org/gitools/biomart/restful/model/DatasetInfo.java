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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datasetInfo", propOrder = {
        "name",
        "displayName",
        "type",
        "visible",
        "_interface"
})
public class DatasetInfo
{

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected String displayName;

    @XmlElement(required = true)
    protected String type;

    protected int visible;

    @XmlElement(name = "interface", required = true)
    protected String _interface;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value)
    {
        this.name = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayName(String value)
    {
        this.displayName = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value)
    {
        this.type = value;
    }

    /**
     * Gets the value of the visible property.
     */
    public int getVisible()
    {
        return visible;
    }

    /**
     * Sets the value of the visible property.
     */
    public void setVisible(int value)
    {
        this.visible = value;
    }

    /**
     * Gets the value of the interface property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getInterface()
    {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInterface(String value)
    {
        this._interface = value;
    }

}
