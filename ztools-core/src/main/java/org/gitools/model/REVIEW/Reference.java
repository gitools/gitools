package org.gitools.model.REVIEW;

import java.net.URI;

import org.gitools.model.Artifact;

public class Reference extends Artifact {

	private static final long serialVersionUID = 4327574491434582643L;

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
