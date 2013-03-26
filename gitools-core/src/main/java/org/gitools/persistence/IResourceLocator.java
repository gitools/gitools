package org.gitools.persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

/**
 * A ResourceLocator contains all the information needed to locate a Gitools resource.
 */
public interface IResourceLocator {

    Reader getReader() throws IOException;

    String getName();

    URL getURL();

    Writer getWriter() throws IOException;

}
