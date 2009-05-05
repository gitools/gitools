package es.imim.bg.ztools.model;

import java.io.Serializable;

public class Group extends Artifact implements Serializable {

	private String source;
	private String organism;
	private String version;

	public Group() {
		super();
	}

	public Group(String id, String artifactType) {
		super(id, artifactType);
	}

	public Group(String id, String artifactType, String title) {
		super(id, artifactType, title);
	}

	public Group(String id, String artifactType, String title,
			String description) {
		super(id, artifactType, title, description);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
