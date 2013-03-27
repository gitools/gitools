/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixFactory;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.persistence.locators.filters.adapters.GzResourceLocatorAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PersistenceManager implements Serializable {

    private static PersistenceManager defaultManager = new PersistenceManager();

    public static PersistenceManager get() {
        return defaultManager;
    }

    private final Map<Class<? extends IResource>, Map<String, IResourceFormat>> formats = new HashMap<Class<? extends IResource>, Map<String, IResourceFormat>>();

    private final Map<String, String> extensionToMime = new HashMap<String, String>();
    private final Map<String, IResourceFormat> mimeToFormat = new HashMap<String, IResourceFormat>();
    private final Map<Class<? extends IResource>, String> classToExtension = new HashMap<Class<? extends IResource>, String>();

    private PersistenceManager() {
    }

    public <R extends IResource> IResourceFormat<R> getFormat(String extension, Class<R> resourceClass) throws PersistenceException {

        // Use only the first part of a composed extension to check the format.
        int composedExtension = extension.indexOf(".");
        if (composedExtension != -1) {
            extension = extension.substring(0, composedExtension);
        }

        Map<String, IResourceFormat> extensions = formats.get(resourceClass);

        if (extensions == null) {

            // Try to deduce from the extension and the parent class
            for (Class<? extends IResource> keyClass : formats.keySet()) {
                if (resourceClass.isAssignableFrom(keyClass) && formats.get(keyClass).containsKey(extension)) {
                    return formats.get(keyClass).get(extension);
                }
            }

            throw new PersistenceException("Unknow resource: '" + resourceClass.getName() + "'");
        }

        IResourceFormat resourceFormat = extensions.get(extension);

        if (resourceFormat == null) {
            throw new PersistenceException("Invalid file extension '" + extension + "' for a resource of type '" + resourceClass.getName());
        }

        return resourceFormat;
    }

    @Deprecated
    public IResourceFormat getFormatByMime(String mime) {
        return mimeToFormat.get(mime);
    }

    @Deprecated
    public String getMimeFromFile(String name) {
        String extension = name.substring(name.indexOf(".") + 1);
        extension = extension.replace(".gz", "");
        return extensionToMime.get(extension);
    }

    public String getDefaultExtension(IResource resource) {
        return classToExtension.get(resource.getClass());
    }


    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor) throws PersistenceException {
        return load(resourceLocator, resourceClass, new Properties(), progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        return load(resourceLocator, resourceClass, getFormat(resourceLocator.getExtension(), resourceClass), properties, progressMonitor);
    }

    public <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IResourceFormat<R> resourceFormat, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {

        // Apply compression adapters if it's required
        resourceLocator = applyAdaptors(resourceLocator);

        // Configure the format
        if (resourceFormat.isConfigurable()) {
            resourceFormat.configure(resourceLocator, resourceClass, properties, progressMonitor);
        }

        // Build the resource
        R resource = resourceFormat.read(resourceLocator, resourceClass, progressMonitor);

        return resource;
    }

    @Deprecated
    public <R extends IResource> R load(File file, String mime, IProgressMonitor progressMonitor) throws PersistenceException {
        IResourceLocator resourceLocator = new UrlResourceLocator(file);
        IResourceFormat<R> resourceFormat = getFormatByMime(mime);

        return load(resourceLocator, resourceFormat.getResourceClass(), resourceFormat, new Properties(), progressMonitor);
    }

    @Deprecated
    public <R extends IResource> R load(File file, Class<R> resourceClass, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        return load(new UrlResourceLocator(file), resourceClass, properties, progressMonitor);
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
        store(resourceLocator, resource, (IResourceFormat<R>) getFormat(resourceLocator.getExtension(), resource.getClass()), progressMonitor);
    }

    public <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException {

        //TODO Look for another way to manage this singular case
        if (resource instanceof IMatrixView) {
            resource = (R) MatrixFactory.create((IMatrixView) resource);
        }

        // Apply compression adapters if it's required
        resourceLocator = applyAdaptors(resourceLocator);

        // Write the resource
        resourceFormat.write(resourceLocator, resource, progressMonitor);

    }


    @Deprecated
    public <R extends IResource> void store(File file, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
        store(new UrlResourceLocator(file), resource, progressMonitor);
    }

    @Deprecated
    public <R extends IResource> void store(File file, String mime, R resource, IProgressMonitor progressMonitor) throws PersistenceException {

        IResourceLocator resourceLocator = new UrlResourceLocator(file);
        IResourceFormat<R> resourceFormat = getFormatByMime(mime);

        store(resourceLocator, resource, resourceFormat, progressMonitor);
    }

    void registerFormat(IResourceFormat resourceFormat) {

        if (!formats.containsKey(resourceFormat.getResourceClass())) {
            formats.put(resourceFormat.getResourceClass(), new HashMap<String, IResourceFormat>());
        }

        Map<String, IResourceFormat> extensions = formats.get(resourceFormat.getResourceClass());
        for (String extension : resourceFormat.getExtensions()) {
            extensions.put(extension, resourceFormat);
        }

        extensionToMime.put(resourceFormat.getDefaultExtension(), resourceFormat.getMime());
        mimeToFormat.put(resourceFormat.getMime(), resourceFormat);
        classToExtension.put(resourceFormat.getResourceClass(), resourceFormat.getDefaultExtension());
    }

    private IResourceLocator applyAdaptors(IResourceLocator resourceLocator) {

        if (GzResourceLocatorAdapter.isAdaptable(resourceLocator.getExtension())) {
            resourceLocator = GzResourceLocatorAdapter.getAdaptor(resourceLocator);
        }

        return resourceLocator;
    }


}
