package org.gitools.model.REVIEW;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.gitools.model.IArtifact;
import org.gitools.model.Publication;


public class Projecte {

	/** Internal id of the project */
	private String id;

	/** Title of the project */
	private String title;

	/** Lab that publishes the project **/
	private String laboratory;

	/** URL of the laboratory **/
	private URL url;

	/** List of publications associated with the project, if any **/
	private List<Publication> publications = new ArrayList<Publication>();

	/** Main artifacts, they would be showed in the breadcrumb **/
	private List<IArtifact> artifacts = new ArrayList<IArtifact>();

	public Projecte() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
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
