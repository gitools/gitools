package org.gitools.resources.factory;

import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class ProjectResourceFactory extends ResourceFactory {

	IResource base;

	public ProjectResourceFactory(IResource baseResource) {
		this.base = baseResource;

	}

	public ProjectResource getResource(String location) {
		if (location.contains(ProjectResource.scheme)) {
			
			String newLocation = location.substring(ProjectResource.scheme.length());
			return new ProjectResource(base, newLocation);
		}
		return null;
	}

}
