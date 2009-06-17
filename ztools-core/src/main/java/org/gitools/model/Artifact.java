package org.gitools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.resources.IResource;

public class Artifact
		extends AbstractModel
		implements Serializable, IArtifact {

	private static final long serialVersionUID = 5752318457428475330L;

	/** id unique **/
	@XmlTransient
	private String id;

	/** The resource containing this artifact. */
	@XmlTransient
	private IResource resource;
	
	/** type of the artifact **/
	@Deprecated
	@XmlTransient
	private String type;

	/** short description **/
	private String title;

	/** long description **/
	private String description;
	
	/** Extra attributes **/
	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	private List<Attribute> attributes = new ArrayList<Attribute>(0);

	/* constructors */

	public Artifact() {

	}

	public Artifact(String id, String artifactType) {
		this(id, artifactType, null, null);
	}

	public Artifact(String id, String artifactType, String title) {
		this(id, artifactType, title, null);
	}

	public Artifact(
			String artifactId, String artifactType, 
			String title, String description) {
		
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

	@Deprecated
	public String getType() {
		return type;
	}

	@Deprecated
	public void setType(String artifactType) {
		this.type = artifactType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
}
