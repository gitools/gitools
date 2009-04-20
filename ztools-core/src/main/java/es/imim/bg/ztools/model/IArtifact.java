package es.imim.bg.ztools.model;

import java.util.List;

public interface IArtifact {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public String getId();

    public void setId(String id);

    public List<Artifact> getArtifactList();

    public void setArtifactList(List<Artifact> artifactList);

    public void add(IArtifact artifact);

}