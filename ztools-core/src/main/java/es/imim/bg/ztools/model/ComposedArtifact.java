package es.imim.bg.ztools.model;

import java.util.ArrayList;
import java.util.List;

public class ComposedArtifact extends Artifact {

    /** an artifact by itself but it can be a container, or a folder p.e **/
      
    private List <IArtifact>  artifactList;
    
    public List<IArtifact> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<IArtifact> artifactList) {
        this.artifactList = artifactList;
    }

    public ComposedArtifact(String id, String artifactType) {
	this(id, artifactType, null, null, new ArrayList<IArtifact>());
    }
   
    public ComposedArtifact(String id, String artifactType, String title, String description) {
	this(id, artifactType, title, description, new ArrayList<IArtifact>());
    }

    public ComposedArtifact(String id, String artifactType, String title, String description,List<IArtifact> artifactList) {
	super(id, artifactType, title, description);
	this.artifactList = artifactList;
    }
    
    public void addArtifact(IArtifact artifact){
	this.artifactList.add(artifact);
    }
    
}
