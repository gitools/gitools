package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project extends Artifact {

	private static final long serialVersionUID = 7978328129043692524L;

	/** List of publications associated with the project, if any **/
	@XmlTransient
	private List<Publication> publications = new ArrayList<Publication>();

	/** List of the laboratories involved in the project, if any **/
	private List<Laboratory> laboratories = new ArrayList<Laboratory>();

	public Project() {

	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public void addPublication(Publication publication) {
		this.publications.add(publication);
	}

	public List<Laboratory> getLaboratories() {
		return laboratories;
	}

	public void setLaboratories(List<Laboratory> laboratories) {
		this.laboratories = laboratories;
	}

	public void addLaboratory(Laboratory laboratory) {
		this.laboratories.add(laboratory);
	}

}
