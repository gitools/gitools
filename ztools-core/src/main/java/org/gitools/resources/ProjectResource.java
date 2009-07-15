package org.gitools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

public class ProjectResource implements IResource{
	
	public static String scheme = "project:";
	
	protected IResource base;
	protected String location;
	private IResource resource;
	
	
	public ProjectResource(IResource base, String location){
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
		return new ProjectResource(base,resource.toURI().toString());
	}

	@Override
	public String toString() {
		return scheme + location;
	}

	@Override
	public IResource relativize(IResource resource) {
		String location = this.resource.toURI().relativize(resource.toURI()).toString();
		return new ProjectResource(base, location );
	}

}
