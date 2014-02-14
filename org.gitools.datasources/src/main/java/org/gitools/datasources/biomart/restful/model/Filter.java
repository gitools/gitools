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
package org.gitools.datasources.biomart.restful.model;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Filter", propOrder = {"name", "value", "excluded"})
public class Filter {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private int excluded;

    // Only in case of radio components since
    // a query in xml with this type of components is different
    @XmlTransient
    private Boolean radio = false;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     * {@link String }
     */
    public Boolean getRadio() {
        return radio;
    }


    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRadio(Boolean radio) {
        this.radio = radio;
    }

    public int getExcluded() {
        return excluded;
    }

    public void setExcluded(int excluded) {
        this.excluded = excluded;
    }

}
