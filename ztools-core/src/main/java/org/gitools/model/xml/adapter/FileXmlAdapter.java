package org.gitools.model.xml.adapter;

import java.io.File;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.persistence.IPathResolver;

public class FileXmlAdapter extends XmlAdapter<String, File> {

	private IPathResolver pathResolver;
	
	public FileXmlAdapter(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Override
	public String marshal(File resource) throws Exception {
		if(resource == null) return null;
		return resource.getName().toString();
	}

	@Override
	public File unmarshal(String location) throws Exception {
		return pathResolver.createResourceFromString(location);
	}

}
