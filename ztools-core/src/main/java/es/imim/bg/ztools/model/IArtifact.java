package es.imim.bg.ztools.model;

public interface IArtifact {
    // Id
    public String getId();
    public void setId(String id);

    // ArtifactType
    public void setArtifactType(String arctType);
    public String getArtifactType();

}