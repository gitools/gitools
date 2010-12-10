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
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MartRegistry")
@XmlAccessorType(XmlAccessType.FIELD)
public class MartRegistry {

	@XmlElement(name = "MartURLLocation")
	protected List<MartLocation> locations;

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
