package org.gitools.model.xml.adapter;

import java.io.File;
import java.net.URI;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.persistence.IPathResolver;

public class FileXmlAdapter extends XmlAdapter<String, File> {

	private IPathResolver pathResolver;
	
	public FileXmlAdapter(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Override
	public String marshal(File resource) throws Exception {
		return resource.toURI().relativize(
				URI.create(pathResolver.pathToString())).toString();
	}

	@Override
	public File unmarshal(String location) throws Exception {
		return pathResolver.stringToPath(location);
	}

}
