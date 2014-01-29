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
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterPage {

    @XmlAttribute
    private String internalName;

    @XmlAttribute
    private String displayName;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private boolean hidden;

    @XmlAttribute
    private boolean hideDisplay;

    @XmlElement(name = "FilterGroup")
    private List<FilterGroup> filterGroups = new ArrayList<FilterGroup>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<FilterGroup> getFilterGroups() {
        return filterGroups;
    }

    public void setFilterGroups(List<FilterGroup> filterGroups) {
        this.filterGroups = filterGroups;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHideDisplay() {
        return hideDisplay;
    }

    public void setHideDisplay(boolean hideDisplay) {
        this.hideDisplay = hideDisplay;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

}
