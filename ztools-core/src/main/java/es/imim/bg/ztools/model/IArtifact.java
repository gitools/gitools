package es.imim.bg.ztools.model;

public interface IArtifact {
    // artifactId
    public String getArtifactId();
    public void setArtifactId(String artifactId);

    // artifactType
    public void setArtifactType(String arctType);
    public String getArtifactType();

    // title
    public void setTitle(String title);
    public String getTitle();
    
    // description
    public void setDescription(String title);
    public String getDescription();

}