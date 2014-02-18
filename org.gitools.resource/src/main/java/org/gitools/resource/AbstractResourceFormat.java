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
package org.gitools.resource;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;

public abstract class AbstractResourceFormat<R extends IResource> implements IResourceFormat<R> {

    private final String extension;
    private final Class<R> resourceClass;

    protected AbstractResourceFormat(String extension, Class<R> resourceClass) {
        this.extension = extension;
        this.resourceClass = resourceClass;
    }

    @Override
    public Class<R> getResourceClass() {
        return resourceClass;
    }

    @Override
    public String getExtension() {
        return extension;
    }


    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean isDefaultExtension() {
        return false;
    }

    @Override
    public final void configure(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        configureResource(resourceLocator, progressMonitor);
    }

    /**
     * Override this methos and {@link #isConfigurable()} if you want to configure the format before calling read
     * write.
     * <p/>
     * This give you a change to read the resource two times (one on configure and the other on reading). But be
     * careful because the resource can be in a remote and slow location.
     *
     * @param resourceLocator the resource locator
     * @param progressMonitor the progress monitor
     * @throws PersistenceException the persistence exception
     */
    protected void configureResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        // Nothing
    }

    @Override
    public final R read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        return readResource(resourceLocator, progressMonitor);
    }

    /**
     * You must override this method if you are implementing a readable format.
     *
     * @param resourceLocator the resource locator
     * @param progressMonitor the progress monitor
     * @return the resource
     * @throws PersistenceException the persistence exception
     */
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
