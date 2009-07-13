package org.gitools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;

public interface IResource extends Serializable {

	Reader openReader() throws FileNotFoundException, IOException;
	
	Writer openWriter() throws FileNotFoundException, IOException;
	
	URI toURI();

	IResource resolve(String str);
	
	IResource relativize(IResource resource);
}
