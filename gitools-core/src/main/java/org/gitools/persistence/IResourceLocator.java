package org.gitools.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A ResourceLocator contains all the information needed to locate a Gitools resource.
 */
public interface IResourceLocator {

    URL getURL();

    String getBaseName();

    String getExtension();

    String getName();

    IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException;

    boolean isWritable();

    InputStream openInputStream() throws IOException;

    OutputStream openOutputStream() throws IOException;
}
