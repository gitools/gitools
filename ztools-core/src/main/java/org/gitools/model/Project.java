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

	/** Lab that publishes the project **/
	private String laboratory;

	/** URL of the laboratory **/
	private String url;

	/** List of publications associated with the project, if any **/
	private List<Publication> publications = new ArrayList<Publication>();

	/** Main artifacts, they would be showed in the breadcrumb **/
	@XmlTransient
	private List<IArtifact> artifacts = new ArrayList<IArtifact>();

	public Project() {

	}

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public List<IArtifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<IArtifact> artifacts) {
		this.artifacts = artifacts;
	}

	public void addArtifact(IArtifact artifact) {
		this.artifacts.add(artifact);
	}

	public void addPublication(Publication publication) {
		this.publications.add(publication);
	}
}
