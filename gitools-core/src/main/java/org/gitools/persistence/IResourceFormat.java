package org.gitools.persistence;

import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Properties;

public interface IResourceFormat<R extends IResource> {

    @Deprecated
    String getMime();

    String getDefaultExtension();

    String[] getExtensions();

    Class<R> getResourceClass();

    boolean isConfigurable();

    void configure(IResourceLocator resourceLocator, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException;

    R read(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) throws PersistenceException;

    void write(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException;

}
