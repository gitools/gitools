package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder={"publications", "laboratories" } )

public class Project extends Artifact {

	private static final long serialVersionUID = 7978328129043692524L;

	/** List of publications associated with the project, if any **/
	
	@XmlElementWrapper(name = "publications")
    @XmlElement(name = "publication")
	private List<Publication> publications = new ArrayList<Publication>();

	/** List of the laboratories involved in the project, if any **/

	@XmlElementWrapper(name = "laboratories")
    @XmlElement(name = "laboratory")
	private List<Laboratory> laboratories = new ArrayList<Laboratory>();

	public Project() {

	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public List<Laboratory> getLaboratories() {
		return laboratories;
	}

	public void setLaboratories(List<Laboratory> laboratories) {
		this.laboratories = laboratories;
	}

}
