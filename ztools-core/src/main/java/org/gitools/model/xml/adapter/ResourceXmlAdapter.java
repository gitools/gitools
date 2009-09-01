package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.resources.IResource;
import org.gitools.resources.factory.ResourceFactory;

public class ResourceXmlAdapter extends XmlAdapter<String, IResource> {

	ResourceFactory resourceFactory;
	
	public ResourceXmlAdapter(ResourceFactory factoryResource) {
		this.resourceFactory = factoryResource;
	}

	@Override
	public String marshal(IResource resource) throws Exception {
		if(resource == null) return null;
		return resource.toString();
		
	}

	@Override
	public IResource unmarshal(String location) throws Exception {
		return resourceFactory.getResource(location);
		
	}

}
