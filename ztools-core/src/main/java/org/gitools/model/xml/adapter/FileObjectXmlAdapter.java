package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.vfs.FileObject;
import org.gitools.persistence.IFileObjectResolver;

//FIXME Review
public class FileObjectXmlAdapter extends XmlAdapter<String, FileObject> {

	private IFileObjectResolver fileObjectResolver;
	
	public FileObjectXmlAdapter(IFileObjectResolver fileObjectResolver) {
		this.fileObjectResolver = fileObjectResolver;
	}

	@Override
	public String marshal(FileObject resource) throws Exception {
		if(resource == null)
			return null;
		
		return resource.getName().toString();
	}

	@Override
	public FileObject unmarshal(String location) throws Exception {
		return fileObjectResolver.createResourceFromString(location);
	}

}
