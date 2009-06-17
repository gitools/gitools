package org.gitools.model;

import java.io.Serializable;

import org.gitools.resources.IResource;

public class ResourceReference implements Serializable {

	private static final long serialVersionUID = -4821090938379782993L;
	
	protected String title;
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
