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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Option extends FilterDescription {

    @XmlAttribute
    private String ref;

    @XmlAttribute
    private String isSelectable;

    @XmlElement(name = "PushAction")
    private List<PushAction> pushactions;

    public Option() {
    }

    public String getIsSelectable() {
        return isSelectable;
    }

    public void setIsSelectable(String isSelectable) {
        this.isSelectable = isSelectable;
    }

    public List<PushAction> getPushactions() {
        return pushactions;
    }

    public void setPushactions(List<PushAction> pushactions) {
        this.pushactions = pushactions;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
