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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Properties;

@XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
public class ResourceReference<R extends IResource>
{

    private transient boolean loaded = false;
    @Nullable
    private transient R resource;

    // New instance
    private String baseName;
    private Class<? extends R> resourceClass;

    // Stored instanc
    private IResourceLocator locator;
    private IResourceFormat<? extends R> resourceFormat;

    private Properties properties = new Properties();

    public ResourceReference(String baseName, @NotNull R resource)
    {
        this(baseName, resource, (Class<? extends R>) resource.getClass());
    }

    public ResourceReference(String baseName, Class<? extends R> resourceClass)
    {
        this(baseName, null, resourceClass);
    }

    private ResourceReference(String baseName, R resource, Class<? extends R> resourceClass)
    {
        this.loaded = true;
        this.baseName = baseName;
        this.resourceClass = resourceClass;
        this.resource = resource;
    }

    public ResourceReference(@NotNull IResourceLocator locator, @NotNull Class<? extends R> resourceClass)
    {
        this(locator, PersistenceManager.get().getFormat(locator.getExtension(), resourceClass));
    }

    public ResourceReference(IResourceLocator locator, IResourceFormat<? extends R> resourceFormat)
    {
        this.loaded = false;

        this.locator = locator;
        this.resourceFormat = resourceFormat;
    }

    @Nullable
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

    public final void setLocator(@NotNull IResourceLocator locator)
    {
        this.locator = locator;
        this.baseName = locator.getBaseName();
    }

    public final Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
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
        IResource loadedResource = PersistenceManager.get().load(locator, resourceFormat, properties, progressMonitor);
        resource = onAfterLoad(loadedResource);
        loaded = true;

    }

    protected R onBeforeLoad(R resource)
    {
        return resource;
    }

    @NotNull
    protected R onAfterLoad(@NotNull IResource resource)
    {
        return (R) resource;
    }

    protected R onBeforeUnload(R resource)
    {
        return resource;
    }

    @Nullable
    protected R onAfterUnload()
    {
        return null;
    }

}
