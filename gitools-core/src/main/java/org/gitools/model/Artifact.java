package org.gitools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "title", "description", "attributes" })
public class Artifact extends AbstractModel {

	private static final long serialVersionUID = 5752318457428475330L;

	public static final String TITLE_CHANGED = "titleChanged";
	public static final String DESC_CHANGED = "descChanged";
	public static final String ATTRIBUTES_CHANGED = "attributesChanged";

	/** short description **/
	protected String title;

	/** long description **/
	protected String description;

	/** Extra attributes **/
	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	protected List<Attribute> attributes = new ArrayList<Attribute>(0);

	/* constructors */

	public Artifact() {
	}

	public Artifact(Artifact artifact) {
		this.title = artifact.getTitle();
		this.description = artifact.getDescription();
		this.attributes = (List<Attribute>) ((ArrayList<Attribute>) artifact.getAttributes()).clone();
	}

	/* getters and setters */

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		firePropertyChange(TITLE_CHANGED);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		firePropertyChange(DESC_CHANGED);
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
		firePropertyChange(ATTRIBUTES_CHANGED);
	}
}
