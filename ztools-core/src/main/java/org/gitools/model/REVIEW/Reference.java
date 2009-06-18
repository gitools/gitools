package org.gitools.model.REVIEW;

import java.net.URI;

import org.gitools.model.Artifact;

@Deprecated
public class Reference extends Artifact {

	private static final long serialVersionUID = 4327574491434582643L;

	private URI extRef;

	public Reference() {

	}

	public URI getExtRef() {
		return extRef;
	}

	public void setExtRef(URI extRef) {
		this.extRef = extRef;
	}

}
