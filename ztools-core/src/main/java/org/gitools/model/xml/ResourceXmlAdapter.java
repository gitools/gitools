package org.gitools.model.xml;

import java.io.File;
import java.net.URI;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

public class ResourceXmlAdapter extends XmlAdapter<String, IResource> {

	protected IResource baseResource;

	public ResourceXmlAdapter() {
	}

	public ResourceXmlAdapter(IResource baseResource) {
		this.baseResource = baseResource;
	}

	@Override
	public String marshal(IResource resource) throws Exception {
		// FIXME: 
		return resource.toURI().toString().replace(
				baseResource.toURI().toString(), "");
	}

	@Override
	public IResource unmarshal(String v) throws Exception {
		URI uri = new URI(v);
		URI path = baseResource.toURI().resolve(uri);
		
		// FIXME: 
		// if (uri.getScheme().equalsIgnoreCase("file")) {
		// if (uri.isAbsolute()){
		return new FileResource(new File(path));
		// }else
		// throw new RuntimeException("relative file URI not supported: " +
		// uri.toString());
		// }
		// throw new RuntimeException("URI Scheme not supported: " +
		// uri.toString());
	}

}
