package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.resources.IResource;
import org.gitools.resources.ProjectResource;

public class ResourceXmlAdapter extends XmlAdapter<String, IResource> {

	protected IResource base;

	public ResourceXmlAdapter() {
	}

	public ResourceXmlAdapter(IResource baseResource) {
		this.base = baseResource;
	}

	@Override
	public String marshal(IResource resource) throws Exception {
		if (resource instanceof ProjectResource)
			return resource.toString();
		return null;
	}

	@Override
	public IResource unmarshal(String location) throws Exception {
		ProjectResource project = new ProjectResource(base, location);
		return project;
		//return new ProjectResource(base, location);
	}

}
