package org.gitools.model;

import java.io.Serializable;

public class Artifact
		extends AbstractModel
		implements Serializable, IArtifact {

	private static final long serialVersionUID = 5752318457428475330L;

	/** id unique **/
	private String id;

	/** type of the artifact **/
	private String type;

	/** short description **/
	private String title;

	/** long description **/
	private String description;

	/* constructors */

	public Artifact() {

	}

	public Artifact(String id, String artifactType) {
		this(id, artifactType, null, null);
	}

	public Artifact(String id, String artifactType, String title) {
		this(id, artifactType, title, null);
	}

	public Artifact(String artifactId, String artifactType, String title,
			String description) {
		super();
		this.id = artifactId;
		this.type = artifactType;
		this.title = title;
		this.description = description;
	}

	/* getters and setters */

	public String getId() {
		return id;
	}

	public void setId(String artifactId) {
		this.id = artifactId;
	}

	public String getType() {
		return type;
	}

	public void setType(String artifactType) {
		this.type = artifactType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
