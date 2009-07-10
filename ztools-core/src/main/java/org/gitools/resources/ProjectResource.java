package org.gitools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

public class ProjectResource implements IResource{
	
	protected IResource base;
	protected String location;
	protected IResource resource;
	
	public ProjectResource(IResource base, String location){
		this.base = base;
		this.location = location;
		this.resource = base.resolve(location);
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
	//	new ProjectResource(base, );
		return this.resource.resolve(str);
	}

	@Override
	public String toString() {
		return location;
	}

	
	
}
