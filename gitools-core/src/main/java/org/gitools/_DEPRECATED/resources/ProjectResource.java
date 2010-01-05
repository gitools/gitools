package org.gitools._DEPRECATED.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

@Deprecated
public class ProjectResource implements IResource {

	private static final long serialVersionUID = 8970466799805795935L;

	public static final String SCHEME = "project";

	protected IResource base;
	protected String location;
	private IResource resource;

	public ProjectResource(IResource base, String location) {
		this.base = base;
		this.location = location;
		this.resource = base.resolve(location);
	}

	public IResource getBase() {
		return base;
	}

	public void setBase(IResource base) {
		this.base = base;
	}

	@Override
	public Reader openReader() throws FileNotFoundException, IOException {
		return resource.openReader();
	}

	@Override
	public Writer openWriter() throws FileNotFoundException, IOException {
		return resource.openWriter();
	}

	@Override
	public URI toURI() {
		return resource.toURI();
	}

	@Override
	public IResource resolve(String str) {
		resource.resolve(str);
		resource.relativize(base);
		return new ProjectResource(base, resource.toURI().toString());
	}

	@Override
	public String toString() {
		return SCHEME + ":" + location;
	}

	@Override
	public IResource relativize(IResource resource) {
		String location = this.resource.toURI().relativize(resource.toURI()).toString();
		return new ProjectResource(base, location);
	}
	
	@Override
	public IResource[] list() {
		return resource.list();
	}
	
	@Override
	public boolean isContainer() {
		return resource.isContainer();
	}
	
	@Override
	public boolean exists() {
		return resource.exists();
	}

	@Override
	public void mkdir() {
		resource.mkdir();
	}
	
	@Override
	public IResource getParent() {
		return resource.getParent();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
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
		ProjectResource other = (ProjectResource) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

}
