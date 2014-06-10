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
package org.gitools.api.resource;

import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
public class ResourceReference<R extends IResource> {

    private transient boolean loaded = false;
    private transient boolean changed = false;


    private transient R resource;

    // New instance
    private String baseName;
    private Class<? extends R> resourceClass;

    // Stored instance
    private IResourceLocator locator;
    private IResourceFormat<? extends R> resourceFormat;

    public ResourceReference(String baseName, R resource) {
        this(baseName, resource, (Class<? extends R>) resource.getClass());
    }

    public ResourceReference(String baseName, Class<? extends R> resourceClass) {
        this(baseName, null, resourceClass);
    }

    public ResourceReference(IResourceLocator locator, Class<? extends R> resourceClass) {
        this(locator, ApplicationContext.getPersistenceManager().getFormat(locator.getExtension(), resourceClass));
    }

    public ResourceReference(IResourceLocator locator, IResourceFormat<? extends R> resourceFormat) {
        this.loaded = false;
        this.baseName = locator.getBaseName();
        this.resourceClass = resourceFormat.getResourceClass();
        this.resource = null;
        this.locator = locator;
        this.resourceFormat = resourceFormat;
    }

    private ResourceReference(String baseName, R resource, Class<? extends R> resourceClass) {
        this.loaded = true;
        this.baseName = baseName;
        this.resourceClass = resourceClass;
        this.resource = resource;
        this.locator = null;

        String extension = ApplicationContext.getPersistenceManager().getDefaultExtension(resourceClass);
        this.resourceFormat = ApplicationContext.getPersistenceManager().getFormat(extension, resourceClass);
    }

    public final R get() {

        if (!isLoaded()) {
            try {
                load(ApplicationContext.getProgressMonitor());
            } catch (PersistenceException e) {
                throw new RuntimeException(e);
            }
        }

        return resource;
    }

    public final String getBaseName() {
        return baseName;
    }

    public final IResourceLocator getLocator() {
        return locator;
    }

    public final Class<? extends IResource> getResourceClass() {
        return this.resourceClass;
    }

    public final void setLocator(IResourceLocator locator) {
        this.locator = locator;
        this.baseName = locator.getBaseName();
    }

    public final IResourceFormat getResourceFormat() {
        return this.resourceFormat;
    }

    public final boolean isLoaded() {
        return loaded;
    }


    public final void unload() {
        resource = onBeforeUnload(resource);
        loaded = false;
        resource = null;
        resource = onAfterUnload();
    }

    public final R load(IProgressMonitor progressMonitor) throws PersistenceException {

        resource = onBeforeLoad(resource);
        IResource loadedResource = ApplicationContext.getPersistenceManager().load(locator, resourceFormat, progressMonitor);
        resource = onAfterLoad(loadedResource);
        loaded = true;

        return resource;
    }

    R onBeforeLoad(R resource) {
        return resource;
    }

    protected R onAfterLoad(IResource resource) {
        return (R) resource;
    }

    R onBeforeUnload(R resource) {
        return resource;
    }


    R onAfterUnload() {
        return null;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
