package org.gitools.resources.factory;

import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class ProjectResourceFactory extends ResourceFactory {

	IResource base;

	public ProjectResourceFactory(IResource baseResource) {
		this.base = baseResource;

	}

	public ProjectResource createResourceFromString(String location) {
		
		if (location.startsWith(ProjectResource.SCHEME + ":")) {
			String newLocation = location.substring(ProjectResource.SCHEME.length() + 1);
			return new ProjectResource(base, newLocation);
		}
		return null;
	}

}
