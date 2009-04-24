package es.imim.bg.ztools.model;

public class Artifact implements IArtifact {

    private String id;
    private String artifactType;

    public Artifact(String id, String artifactType) {
	this.id = id;
	this.artifactType = artifactType;

    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getArtifactType() {
	return artifactType;
    }

    public void setArtifactType(String artifactType) {
	this.artifactType = artifactType;
    }

}
