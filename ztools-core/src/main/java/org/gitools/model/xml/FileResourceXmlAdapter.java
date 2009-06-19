package org.gitools.model.xml;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.resources.FileResource;


public class FileResourceXmlAdapter extends XmlAdapter<FileResource, String>  {
	@Override
	public String unmarshal(FileResource v) throws Exception {
		return v.toURI().toString();
	}

	@Override
	public FileResource marshal(String v) throws Exception {
		//FIXME: se ha de tener en cuenta el contexto de la aplicacion 
		// también  no devolver siempre un FileResource
		return  new FileResource(v);
	}

}
