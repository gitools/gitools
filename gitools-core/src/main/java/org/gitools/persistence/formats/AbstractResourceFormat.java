package org.gitools.persistence.formats;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.util.Properties;

public abstract class AbstractResourceFormat<R extends IResource> implements IResourceFormat<R> {

    private String mime;
    private String extension;
    private Class<R> resourceClass;

    protected AbstractResourceFormat(String extension, String mime, Class<R> resourceClass) {
        this.extension = extension;
        this.mime = mime;
        this.resourceClass = resourceClass;
    }

    @Override
    public String getMime() {
        return mime;
    }

    @Override
    public Class<R> getResourceClass() {
        return resourceClass;
    }

    @Override
    public String getDefaultExtension() {
        return extension;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{extension};
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public final void configure(IResourceLocator resourceLocator, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        configureResource(resourceLocator, properties, progressMonitor);
    }

    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        // Override this method and isConfigurable if you want to configure the format before calling read or write
    }

    @Override
    public final R read(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) throws PersistenceException {
        return readResource(resourceLocator, progressMonitor);
    }

    protected R readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        throw new UnsupportedOperationException("This format don't support reading");
    }

    @Override
    public final void write(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
        writeResource(resourceLocator, resource, progressMonitor);
    }

    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
        throw new UnsupportedOperationException("This format don't support writing");
    }

}
