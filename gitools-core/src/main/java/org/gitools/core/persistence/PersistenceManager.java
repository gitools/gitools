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

import org.gitools.core.persistence.formats.analysis.AbstractXmlFormat;
import org.gitools.core.persistence.locators.UrlResourceLocator;
import org.gitools.core.persistence.locators.filters.IResourceFilter;
import org.gitools.core.persistence.locators.filters.cache.CacheResourceManager;
import org.gitools.core.persistence.locators.filters.gz.GzResourceFilter;
import org.gitools.core.persistence.locators.filters.zip.ZipResourceFilter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class PersistenceManager implements Serializable {

    @NotNull
    private static final PersistenceManager defaultManager = new PersistenceManager();

    @NotNull
    public static PersistenceManager get() {
        return defaultManager;
    }

    private final List<IResourceFilter> filters = new ArrayList<IResourceFilter>();
    private final Map<Class<? extends IResource>, Map<String, IResourceFormat>> formats = new HashMap<Class<? extends IResource>, Map<String, IResourceFormat>>();
    private final Map<Class<? extends IResource>, String> classToDefaultExtension = new HashMap<Class<? extends IResource>, String>();

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

            throw new PersistenceException("Unknow resource class: '" + resourceClass.getName() + "'");
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

        throw new PersistenceException("Class '" + resourceClass + "' is not registered");
    }

    @Deprecated
    public <R extends IResource> R load(@NotNull File file, @NotNull Class<R> resourceClass, @NotNull Properties properties, IProgressMonitor progressMonitor) {
        return load(new UrlResourceLocator(file), getFormat(file.getName(), resourceClass), properties, progressMonitor);
    }

    public <R extends IResource> R load(@NotNull IResourceLocator resourceLocator, @NotNull Class<R> resourceClass, IProgressMonitor progressMonitor) {
        return load(resourceLocator, getFormat(resourceLocator.getExtension(), resourceClass), progressMonitor);
    }

    public <R extends IResource> R load(@NotNull IResourceLocator resourceLocator, @NotNull IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException {
        return load(resourceLocator, resourceFormat, new Properties(), progressMonitor);
    }

    public <R extends IResource> R load(@NotNull IResourceLocator resourceLocator, @NotNull IResourceFormat<R> resourceFormat, @NotNull Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {

        // Add filters
        IResourceLocator filteredResourceLocator = applyFilters(resourceLocator);

        // Use cached locator if exists
        filteredResourceLocator = CacheResourceManager.get().getCacheResourceLocator(filteredResourceLocator);

        // Configure the format
        if (resourceFormat.isConfigurable()) {
            resourceFormat.configure(filteredResourceLocator, properties, progressMonitor);
        }

        // Build the resource
        R resource = resourceFormat.read(filteredResourceLocator, progressMonitor);

        // Set the original locator without the filters.
        resource.setLocator(resourceLocator);

        return resource;
    }

    public <R extends IResource> void store(@NotNull IResourceLocator resourceLocator, @NotNull R resource, IProgressMonitor progressMonitor) throws PersistenceException {
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

    void registerFormat(@NotNull IResourceFormat resourceFormat) {

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

    @NotNull
    private IResourceLocator applyFilters(@NotNull IResourceLocator resourceLocator) {
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
