package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import java.util.Map;
import java.util.Properties;

public interface IResourcePersistence<R> {

    R read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException;

    void write(IResourceLocator file, R resource, IProgressMonitor progressMonitor) throws PersistenceException;

    @Deprecated
    void setPersistenceManager(PersistenceManager manager);

    @Deprecated
    void setProperties(Properties properties);

    @Deprecated
    Map<String, Object> readMetadata(IResourceLocator resourceLocator, String[] keys, IProgressMonitor progressMonitor) throws PersistenceException;


}
