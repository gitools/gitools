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
package org.gitools.core.persistence;

import org.gitools.core.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.ProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
public class ResourceReference<R extends IResource> {

    private transient boolean loaded = false;
    private transient boolean changed = true;

    @Nullable
    private transient R resource;

    // New instance
    private String baseName;
    private Class<? extends R> resourceClass;

    // Stored instance
    private IResourceLocator locator;
    private IResourceFormat<? extends R> resourceFormat;

    public ResourceReference(String baseName, @NotNull R resource) {
        this(baseName, resource, (Class<? extends R>) resource.getClass());
    }

    public ResourceReference(String baseName, Class<? extends R> resourceClass) {
        this(baseName, null, resourceClass);
    }

    public ResourceReference(@NotNull IResourceLocator locator, @NotNull Class<? extends R> resourceClass) {
        this(locator, PersistenceManager.get().getFormat(locator.getExtension(), resourceClass));
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
        this.resourceFormat = null;
    }


    @Nullable
    public final R get() {

        if (!isLoaded()) {
            try {
                load(ProgressMonitor.get());
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

    public final void load(IProgressMonitor progressMonitor) throws PersistenceException {

        resource = onBeforeLoad(resource);
        IResource loadedResource = PersistenceManager.get().load(locator, resourceFormat, progressMonitor);
        resource = onAfterLoad(loadedResource);
        loaded = true;

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

    @Nullable
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
