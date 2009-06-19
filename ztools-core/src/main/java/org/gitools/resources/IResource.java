package org.gitools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.ResourceXmlAdapter;
@XmlJavaTypeAdapter(ResourceXmlAdapter.class)
public interface IResource extends Serializable {

	Reader openReader() throws FileNotFoundException, IOException;
	
	Writer openWriter() throws FileNotFoundException, IOException;
	
	URI toURI();
	
}
