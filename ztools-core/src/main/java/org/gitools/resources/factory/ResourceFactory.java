package org.gitools.resources.factory;

import org.gitools.resources.IResource;

public abstract class ResourceFactory {

	public ResourceFactory(){}
	
	public abstract IResource getResource(String location);
	
}
