package org.gitools.model.REVIEW;

import java.util.ArrayList;
import java.util.List;

import org.gitools.model.Artifact;
import org.gitools.model.IArtifact;

public class ComposedArtifact extends Artifact {

	/** An artifact by itself but it also can be a container, like a folder p.e **/

	private List<IArtifact> artifactList = new ArrayList<IArtifact>();

	public ComposedArtifact() {
		super();
	}

	public void addArtifact(IArtifact artifact) {
		this.artifactList.add(artifact);
	}

	public List<IArtifact> getArtifactList() {
		return artifactList;
	}

	public void setArtifactList(List<IArtifact> artifactList) {
		this.artifactList = artifactList;
	}
}
