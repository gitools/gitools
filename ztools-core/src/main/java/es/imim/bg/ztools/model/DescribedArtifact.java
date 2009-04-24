package es.imim.bg.ztools.model;

public class DescribedArtifact implements IArtifact {

    private IArtifact artifact;
    private String title;
    private String description;

    public DescribedArtifact(IArtifact artifact, String title, String description) {
	this.artifact = artifact;
	this.title = title;
	this.description = description;
    }

    @Override
    public String getArtifactType() {
	return artifact.getArtifactType();
    }

    @Override
    public String getId() {
	return artifact.getId();
    }

    @Override
    public void setArtifactType(String arctType) {
	artifact.setArtifactType(arctType);
    }

    @Override
    public void setId(String id) {
	artifact.setId(id);
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public IArtifact getArtifact() {
	return artifact;
    }

    public String getTitle() {
	return title;
    }

}
