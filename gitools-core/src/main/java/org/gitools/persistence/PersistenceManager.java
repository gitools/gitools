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

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.persistence.formats.analysis.AbstractXmlFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.persistence.locators.filters.IResourceFilter;
import org.gitools.persistence.locators.filters.cache.CacheResourceManager;
import org.gitools.persistence.locators.filters.gz.GzResourceFilter;
import org.gitools.persistence.locators.filters.zip.ZipResourceFilter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenceManager implements Serializable {


    private static final PersistenceManager defaultManager = new PersistenceManager();


    public static PersistenceManager get() {
        return defaultManager;
    }

    private final List<IResourceFilter> filters = new ArrayList<>();
    private final Map<Class<? extends IResource>, Map<String, IResourceFormat>> formats = new HashMap<>();
    private final Map<Class<? extends IResource>, String> classToDefaultExtension = new HashMap<>();

    private PersistenceManager() {
    }

    public <R extends IResource> IResourceFormat<R> getFormat(String fileNameOrExtension, Class<R> resourceClass) {
        if (resourceClass == null) {
            resourceClass = (Class<R>) IResource.class;
        }

        String extension = getFormatExtension(fileNameOrExtension);

        Map<String, IResourceFormat> extensions = formats.get(resourceClass);

        if (extensions == null) {

            // Try to deduce from the extension and the parent class
            for (Class<? extends IResource> keyClass : formats.keySet()) {
                if (resourceClass.isAssignableFrom(keyClass) && formats.get(keyClass).containsKey(extension)) {
                    return formats.get(keyClass).get(extension);
                }
            }

            for (Class<? extends IResource> keyClass : formats.keySet()) {
                if (keyClass.isAssignableFrom(resourceClass) && formats.get(keyClass).containsKey(extension)) {
                    return formats.get(keyClass).get(extension);
                }
            }

            return null;
        }

        IResourceFormat resourceFormat = extensions.get(extension);

        if (resourceFormat == null) {
            // Return the default format for the given resource class
            String defaultExtension = getDefaultExtension(resourceClass);

            resourceFormat = extensions.get(defaultExtension);
        }

        return resourceFormat;
    }

    public String getFormatExtension(String fileNameOrExtension) {
        String extension = removeFiltersExtensions(fileNameOrExtension);

        int dot = extension.lastIndexOf(".");
        if (dot != -1) {
            extension = extension.substring(dot + 1);
        }

        return extension;
    }

    public String getDefaultExtension(Class<? extends IResource> resourceClass) {
        if (classToDefaultExtension.containsKey(resourceClass)) {
            return classToDefaultExtension.get(resourceClass);
        }

        // Look for a class high in the hierarchy
        for (Class<? extends IResource> keyClass : classToDefaultExtension.keySet()) {

            if (keyClass.isAssignableFrom(resourceClass)) {
                return classToDefaultExtension.get(keyClass);
            }
        }

        throw new RuntimeException("Class '" + resourceClass + "' is not registered");
    }

    @Deprecated
    public <R extends IResource> R load(File file, Class<R> resourceClass, IProgressMonitor progressMonitor) {
        return load(new UrlResourceLocator(file), getFormat(file.getName(), resourceClass), progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) {
        return load(resourceLocator, getFormat(resourceLocator.getExtension(), resourceClass), progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException {

        // Use cached locator if exists
        IResourceLocator filteredResourceLocator = CacheResourceManager.get().getCacheResourceLocator(resourceLocator);

        // Add filters
        filteredResourceLocator = applyFilters(filteredResourceLocator);

        // Configure the format
        if (resourceFormat.isConfigurable()) {
            resourceFormat.configure(filteredResourceLocator, progressMonitor);
        }

        // Build the resource
        IProgressMonitor subTask = progressMonitor.subtask();
        subTask.begin("Reading " + filteredResourceLocator.getName(), filteredResourceLocator.getContentLength());
        R resource = resourceFormat.read(filteredResourceLocator, subTask);
        subTask.end();

        // Set the original locator without the filters.
        resource.setLocator(resourceLocator);

        return resource;
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
        store(resourceLocator, resource, (IResourceFormat<R>) getFormat(resourceLocator.getExtension(), resource.getClass()), progressMonitor);
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException {
        if (resourceFormat == null) {
            resourceFormat = (IResourceFormat<R>) getFormat(resourceLocator.getName(), resource.getClass());
        }

        // Add filters
        resourceLocator = applyFilters(resourceLocator);

        // Write the resource
        resourceFormat.write(resourceLocator, resource, progressMonitor);

    }

    void registerFormat(IResourceFormat resourceFormat) {

        if (!formats.containsKey(resourceFormat.getResourceClass())) {
            formats.put(resourceFormat.getResourceClass(), new HashMap<String, IResourceFormat>());
        }

        Map<String, IResourceFormat> extensions = formats.get(resourceFormat.getResourceClass());
        extensions.put(resourceFormat.getExtension(), resourceFormat);

        //TODO use filters interface or register at PersistenceInitialization
        if (resourceFormat instanceof AbstractXmlFormat) {
            registerDefaultExtension(resourceFormat.getResourceClass(), resourceFormat.getExtension() + "." + ZipResourceFilter.SUFFIX);
        } else {
            registerDefaultExtension(resourceFormat.getResourceClass(), resourceFormat.getExtension() + "." + GzResourceFilter.SUFFIX);
        }
    }

    void registerDefaultExtension(Class<? extends IResource> resourceClass, String extension) {
        classToDefaultExtension.put(resourceClass, extension);
    }

    void registerResourceFilter(IResourceFilter resourceFilter) {
        filters.add(resourceFilter);
    }


    private IResourceLocator applyFilters(IResourceLocator resourceLocator) {
        for (IResourceFilter filter : filters) {
            resourceLocator = filter.apply(resourceLocator);
        }

        return resourceLocator;
    }

    private String removeFiltersExtensions(String extension) {
        for (IResourceFilter filter : filters) {
            extension = filter.removeExtension(extension);
        }

        return extension;
    }

}
