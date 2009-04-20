package es.imim.bg.ztools.model;

import java.util.ArrayList;
import java.util.List;

public class Artifact implements IArtifact {

    private String name;
    private String description;
    private String id;

    private List<Artifact> artifactList;

    public Artifact(String id, String name, String description) {

	this.id = id;
	this.name = name;
	this.description = description;
	this.artifactList = new ArrayList<Artifact>();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public List<Artifact> getArtifactList() {
	return artifactList;
    }

    public void setArtifactList(List<Artifact> artifactList) {
	this.artifactList = artifactList;
    }

    public void add(IArtifact artifact) {
	this.artifactList.add((Artifact) artifact);
	
    }



}
