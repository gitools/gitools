package org.gitools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.gitools.resources.IResource;

public class Artifact extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 5752318457428475330L;

	/** The resource containing this artifact. */
	@XmlTransient
	private IResource resource;

	/** short description **/
	private String title;

	/** long description **/
	private String description;

	/**
	 * Extra attributes
	 * 
	 * @XmlElementWrapper(name = "attributes")
	 * @XmlElement(name = "attribute")
	 * **/

	@XmlTransient
	private List<Attribute> attributes = new ArrayList<Attribute>(0);

	/* constructors */

	public Artifact() {

	}

	/* getters and setters */

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
