package es.imim.bg.ztools.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.imim.bg.ztools.model.IArtifact;
import es.imim.bg.ztools.model.Publication;

public class Projecte {

	/** Title of the project*/
	protected String title;

	protected String laboratory;
	protected URL url;

	protected List<Publication> publications = new ArrayList<Publication>();
	protected List<IArtifact> artifacts = new ArrayList<IArtifact>();

	public Projecte() {

	}

	public Projecte(String title) {
		this.title = title;
	}

	public Projecte(String title, String laboratory, URL url,
			List<Publication> publications, List<IArtifact> artifacts) {
		this.title = title;
		this.laboratory = laboratory;
		this.url = url;
		this.publications = publications;
		this.artifacts = artifacts;
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
