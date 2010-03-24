/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.gitools.biomart.soap.model.Mart;

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
