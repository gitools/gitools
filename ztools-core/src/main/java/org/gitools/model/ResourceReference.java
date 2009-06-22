package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.ResourceXmlAdapter;
import org.gitools.resources.IResource;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceReference implements Serializable {

	private static final long serialVersionUID = -4821090938379782993L;
	
	protected String title;
	
	@XmlJavaTypeAdapter(ResourceXmlAdapter.class)
	protected IResource resource;
	
	public ResourceReference() {
	}
	
	public ResourceReference(String title, IResource resource) {
		this.title = title;
		this.resource = resource;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public IResource getResource() {
		return resource;
	}
	
	public void setResource(IResource resource) {
		this.resource = resource;
	}
}
