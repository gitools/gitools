/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.persistence.formats;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Properties;

public abstract class AbstractResourceFormat<R extends IResource> implements IResourceFormat<R>
{

    private String mime;
    private String extension;
    private Class<R> resourceClass;

    protected AbstractResourceFormat(String extension, String mime, Class<R> resourceClass)
    {
        this.extension = extension;
        this.mime = mime;
        this.resourceClass = resourceClass;
    }

    @Override
    public String getMime()
    {
        return mime;
    }

    @Override
    public Class<R> getResourceClass()
    {
        return resourceClass;
    }

    @Override
    public String getDefaultExtension()
    {
        return extension;
    }

    @Override
    public String[] getExtensions()
    {
        return new String[]{extension};
    }

    @Override
    public boolean isConfigurable()
    {
        return false;
    }

    @Override
    public final void configure(IResourceLocator resourceLocator, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        configureResource(resourceLocator, properties, progressMonitor);
    }

    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        // Override this method and isConfigurable if you want to configure the format before calling read or write
    }

    @Override
    public final R read(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) throws PersistenceException
    {
        return readResource(resourceLocator, progressMonitor);
    }

    protected R readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException
    {
        throw new UnsupportedOperationException("This format don't support reading");
    }

    @Override
    public final void write(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException
    {
        writeResource(resourceLocator, resource, progressMonitor);
    }

    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException
    {
        throw new UnsupportedOperationException("This format don't support writing");
    }

}
