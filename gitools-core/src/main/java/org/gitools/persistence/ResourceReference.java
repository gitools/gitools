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
package org.gitools.persistence;

import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.ProgressMonitor;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
public class ResourceReference<R extends IResource>
{

    private transient boolean loaded = false;
    private transient R resource;

    private Class<R> resourceClass;

    private String baseName;
    private IResourceLocator locator;

    public ResourceReference(String baseName, Class<R> resourceClass)
    {
        this(null, baseName, resourceClass, null, true);
    }

    public ResourceReference(String baseName, R resource)
    {
        this(null, baseName, (Class<R>) resource.getClass(), resource, true);
    }

    public ResourceReference(IResourceLocator locator)
    {
        this(locator, locator.getName(), (Class<R>) PersistenceManager.get().getClassFromLocator(locator), null, false);
    }

    public ResourceReference(IResourceLocator locator, R resource)
    {
        this(locator, locator.getName(), (Class<R>) resource.getClass(), resource, true);
    }

    public ResourceReference(IResourceLocator locator, Class<R> resourceClass)
    {
        this(locator, locator.getName(), resourceClass, null, false);
    }

    private ResourceReference(IResourceLocator locator, String baseName, Class<R> resourceClass, R resource, boolean loaded)
    {
        this.baseName = baseName;
        this.locator = locator;
        this.resourceClass = resourceClass;
        this.resource = resource;
        this.loaded = loaded;
    }

    public final R get()
    {

        if (!isLoaded())
        {
            try
            {
                load(ProgressMonitor.get());
            } catch (PersistenceException e)
            {
                throw new RuntimeException(e);
            }
        }

        return resource;
    }

    public final String getBaseName()
    {
        return baseName;
    }

    public final IResourceLocator getLocator()
    {
        return locator;
    }

    public final Class<? extends IResource> getResourceClass()
    {
        return this.resourceClass;
    }

    public final void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
        this.baseName = locator.getBaseName();
    }

    public final boolean isLoaded()
    {
        return loaded;
    }


    public final void unload()
    {
        resource = onBeforeUnload(resource);
        loaded = false;
        resource = null;
        resource = onAfterUnload();
    }

    public final void load(IProgressMonitor progressMonitor) throws PersistenceException
    {

        resource = onBeforeLoad(resource);
        IResource loadedResource = PersistenceManager.get().load(locator, resourceClass, progressMonitor);
        resource = onAfterLoad(loadedResource);
        loaded = true;

    }

    protected R onBeforeLoad(R resource)
    {
        return resource;
    }

    protected R onAfterLoad(IResource resource)
    {
        return (R) resource;
    }

    protected R onBeforeUnload(R resource)
    {
        return resource;
    }

    protected R onAfterUnload()
    {
        return null;
    }


}
