package org.gitools.model;

import java.io.File;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.xml.adapter.FileXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceReference implements Serializable {

	private static final long serialVersionUID = -4821090938379782993L;
	
	protected String title;
	
	@XmlJavaTypeAdapter(FileXmlAdapter.class)
	protected File resource;
	
	public ResourceReference() {
	}
	
	public ResourceReference(String title, File resource) {
		this.title = title;
		this.resource = resource;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public File getResource() {
		return resource;
	}
	
	public void setResource(File resource) {
		this.resource = resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceReference other = (ResourceReference) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
