package org.gitools.model;

public interface IArtifact {
	// artifactId
	public String getId();

	public void setId(String id);

	// artifactType
	public void setType(String type);

	public String getType();

	// title
	public void setTitle(String title);

	public String getTitle();

	// description
	public void setDescription(String title);

	public String getDescription();

}