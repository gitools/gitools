package org.gitools.persistence.locators.filters.adapters;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzResourceLocatorAdapter implements IResourceLocator {

    private String name;
    private String extension;
    private IResourceLocator resourceLocator;

    public static boolean isAdaptable(String resourceExtension) {
        return resourceExtension.endsWith(".gz");
    }

    public static IResourceLocator getAdaptor(IResourceLocator resourceLocator) {
        return new GzResourceLocatorAdapter(resourceLocator);
    }

    private GzResourceLocatorAdapter(IResourceLocator resourceLocator) {
        this.resourceLocator = resourceLocator;
        this.name = resourceLocator.getName().replace(".gz", "");
        this.extension = resourceLocator.getExtension().replace(".gz", "");
    }

    @Override
    public URL getURL() {
        return resourceLocator.getURL();
    }

    @Override
    public String getBaseName() {
        return resourceLocator.getBaseName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return resourceLocator.getReferenceLocator(referenceName);
    }

    @Override
    public boolean isWritable() {
        return resourceLocator.isWritable();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new GZIPInputStream(resourceLocator.openInputStream());
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return new GZIPOutputStream(resourceLocator.openOutputStream());
    }

}
