package org.gitools.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "title", "description", "attributes" })

public class Artifact extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 5752318457428475330L;

	/** short description **/
	protected String title;

	/** long description **/
	protected String description;

	/** Extra attributes **/

	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	private List<Attribute> attributes = null;

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
