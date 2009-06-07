package org.gitools.model;

import java.net.URI;

public class Reference extends Artifact {

	private URI extRef;

	public Reference() {

	}

	public Reference(String artifactId, String artifactType) {
		super(artifactId, artifactType);
	}

	public Reference(String artifactId, String artifactType, String title) {
		super(artifactId, artifactType, title);
	}

	public Reference(String artifactId, String artifactType, String title,
			String description) {
		super(artifactId, artifactType, title, description);
	}

	public URI getExtRef() {
		return extRef;
	}

	public void setExtRef(URI extRef) {
		this.extRef = extRef;
	}

}
