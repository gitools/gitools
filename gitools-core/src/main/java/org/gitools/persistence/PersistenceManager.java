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

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixFactory;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.persistence.locators.filters.adapters.GzResourceLocatorAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PersistenceManager implements Serializable
{

    private static PersistenceManager defaultManager = new PersistenceManager();

    public static PersistenceManager get()
    {
        return defaultManager;
    }

    private final Map<Class<? extends IResource>, Map<String, IResourceFormat>> formats = new HashMap<Class<? extends IResource>, Map<String, IResourceFormat>>();

    private final Map<String, String> extensionToMime = new HashMap<String, String>();
    private final Map<String, IResourceFormat> mimeToFormat = new HashMap<String, IResourceFormat>();
    private final Map<Class<? extends IResource>, String> classToExtension = new HashMap<Class<? extends IResource>, String>();

    private PersistenceManager()
    {
    }

    public <R extends IResource> IResourceFormat<R> getFormat(String extension, Class<R> resourceClass) throws PersistenceException
    {

        // Use only the first part of a composed extension to check the format.
        int composedExtension = extension.lastIndexOf(".");
        if (composedExtension != -1)
        {
            extension = extension.substring(0, composedExtension);

            composedExtension = extension.lastIndexOf(".");
            if (composedExtension != -1) {
                extension = extension.substring(composedExtension + 1);
            }
        }

        Map<String, IResourceFormat> extensions = formats.get(resourceClass);

        if (extensions == null)
        {

            // Try to deduce from the extension and the parent class
            for (Class<? extends IResource> keyClass : formats.keySet())
            {
                if (resourceClass.isAssignableFrom(keyClass) && formats.get(keyClass).containsKey(extension))
                {
                    return formats.get(keyClass).get(extension);
                }
            }

            throw new PersistenceException("Unknow resource: '" + resourceClass.getName() + "'");
        }

        IResourceFormat resourceFormat = extensions.get(extension);

        if (resourceFormat == null)
        {
            throw new PersistenceException("Invalid file extension '" + extension + "' for a resource of type '" + resourceClass.getName());
        }

        return resourceFormat;
    }

    /**
     * @param mime
     * @return
     * @deprecated Use {@link #getClassFromLocator(IResourceLocator)} instead.
     */
    public IResourceFormat getFormatByMime(String mime)
    {
        return mimeToFormat.get(mime);
    }

    /**
     * @param name
     * @return
     * @deprecated Use {@link #getClassFromLocator(IResourceLocator)} instead.
     */
    public String getMimeFromFile(String name)
    {
        String extension = name.substring(name.indexOf(".") + 1);
        extension = extension.replace(".gz", "");
        return extensionToMime.get(extension);
    }

    public <R extends IResource> Class<R> getClassFromLocator(IResourceLocator resourceLocator)
    {
        return getFormatByMime(getMimeFromFile(resourceLocator.getName())).getResourceClass();
    }


    public String getDefaultExtension(IResource resource)
    {
        return getDefaultExtension(resource.getClass());
    }

    public String getDefaultExtension(Class<? extends IResource> resourceClass)
    {
        return classToExtension.get(resourceClass);
    }


    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) throws PersistenceException
    {
        return load(resourceLocator, resourceClass, new Properties(), progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        return load(resourceLocator, resourceClass, getFormat(resourceLocator.getExtension(), resourceClass), properties, progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IResourceFormat<R> resourceFormat, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {

        // Apply compression filters if it's required
        IResourceLocator filteredResourceLocator = applyFilters(resourceLocator);

        // Configure the format
        if (resourceFormat.isConfigurable())
        {
            resourceFormat.configure(filteredResourceLocator, resourceClass, properties, progressMonitor);
        }

        // Build the resource
        R resource = resourceFormat.read(filteredResourceLocator, resourceClass, progressMonitor);

        // Set the original locator without the filters.
        resource.setLocator(resourceLocator);

        return resource;
    }

    @Deprecated
    public <R extends IResource> R load(File file, String mime, IProgressMonitor progressMonitor) throws PersistenceException
    {
        IResourceLocator resourceLocator = new UrlResourceLocator(file);
        IResourceFormat<R> resourceFormat = getFormatByMime(mime);

        return load(resourceLocator, resourceFormat.getResourceClass(), resourceFormat, new Properties(), progressMonitor);
    }

    @Deprecated
    public <R extends IResource> R load(File file, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        return load(new UrlResourceLocator(file), resourceClass, properties, progressMonitor);
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException
    {
        store(resourceLocator, resource, (IResourceFormat<R>) getFormat(resourceLocator.getExtension(), resource.getClass()), progressMonitor);
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException
    {

        //TODO Look for another way to manage this singular case
        if (resource instanceof IMatrixView)
        {
            resource = (R) MatrixFactory.create((IMatrixView) resource);
        }

        // Apply compression adapters if it's required
        resourceLocator = applyFilters(resourceLocator);

        // Write the resource
        resourceFormat.write(resourceLocator, resource, progressMonitor);

    }


    @Deprecated
    public <R extends IResource> void store(File file, R resource, IProgressMonitor progressMonitor) throws PersistenceException
    {
        store(new UrlResourceLocator(file), resource, progressMonitor);
    }

    @Deprecated
    public <R extends IResource> void store(File file, String mime, R resource, IProgressMonitor progressMonitor) throws PersistenceException
    {

        IResourceLocator resourceLocator = new UrlResourceLocator(file);
        IResourceFormat<R> resourceFormat = getFormatByMime(mime);

        store(resourceLocator, resource, resourceFormat, progressMonitor);
    }

    void registerFormat(IResourceFormat resourceFormat)
    {

        if (!formats.containsKey(resourceFormat.getResourceClass()))
        {
            formats.put(resourceFormat.getResourceClass(), new HashMap<String, IResourceFormat>());
        }

        Map<String, IResourceFormat> extensions = formats.get(resourceFormat.getResourceClass());
        for (String extension : resourceFormat.getExtensions())
        {
            extensions.put(extension, resourceFormat);
        }

        extensionToMime.put(resourceFormat.getDefaultExtension(), resourceFormat.getMime());
        mimeToFormat.put(resourceFormat.getMime(), resourceFormat);
        classToExtension.put(resourceFormat.getResourceClass(), resourceFormat.getDefaultExtension());
    }

    private IResourceLocator applyFilters(IResourceLocator resourceLocator)
    {

        if (GzResourceLocatorAdapter.isAdaptable(resourceLocator.getExtension()))
        {
            resourceLocator = GzResourceLocatorAdapter.getAdaptor(resourceLocator);
        }

        return resourceLocator;
    }
}
