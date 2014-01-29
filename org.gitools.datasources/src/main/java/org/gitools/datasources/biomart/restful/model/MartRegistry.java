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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement(name = "MartRegistry")
@XmlAccessorType(XmlAccessType.FIELD)
public class MartRegistry {

    @XmlElement(name = "MartURLLocation")
    private List<MartLocation> locations;

    public MartRegistry() {
        this.locations = new ArrayList<MartLocation>();
    }

    public MartRegistry(MartLocation[] locations) {
        this.locations = new ArrayList<MartLocation>(Arrays.asList(locations));
    }

    public MartRegistry(List<MartLocation> locations) {
        this.locations = locations;
    }

	/*public MartRegistry(List<Mart> marts) {
        this.locations = new ArrayList<MartLocation>(marts.size());
		for (Mart mart : marts)
			locations.add(new MartLocation(mart));
	}*/

    public List<MartLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<MartLocation> locations) {
        this.locations = locations;
    }
}
