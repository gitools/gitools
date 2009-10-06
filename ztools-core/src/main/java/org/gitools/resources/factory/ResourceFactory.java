package org.gitools.resources.factory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class ResourceFactory implements IResourceFactory {
	
	private static final Map<String, Class<? extends IResourceFactory>> schemeFactories =
		new HashMap<String, Class<? extends IResourceFactory>>();
	
	static {
		schemeFactories.put(ProjectResource.SCHEME, ProjectResourceFactory.class);
	}
	
	@Override
	public IResource createResourceFromString(String location) {
		IResource resource = null;
		int pos = location.indexOf(':');
		if (pos > 0) {
			String scheme = location.substring(0, pos);
			IResourceFactory factory = createResourceFactory(scheme);
			
			if (factory != null)
				resource = factory.createResourceFromString(location);
		}
		else if (pos == -1 && location.startsWith(File.separator)) {
			resource = new FileResource(location.substring(pos + 1));
		}
		return resource;
	}
	
	protected IResourceFactory createResourceFactory(String scheme) {
		IResourceFactory factory = null;
		
		Class<? extends IResourceFactory> resourceFactoryClass =
			schemeFactories.get(scheme);
		
		if (resourceFactoryClass != null) {	
			try {
				factory = resourceFactoryClass.newInstance();
			} catch (Exception e) {}
		}
		
		return factory;
	}

}
