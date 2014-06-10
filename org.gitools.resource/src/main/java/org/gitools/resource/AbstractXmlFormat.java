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

import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import org.apache.commons.io.IOUtils;
import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.plugins.IPlugin;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractXmlFormat<R extends IResource> extends AbstractResourceFormat<R> {
    private List<ResourceReference<? extends IResource>> dependencies;

    protected AbstractXmlFormat(String extension, Class<R> resourceClass) {
        super(extension, resourceClass);
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public boolean isDefaultExtension() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    public void beforeRead(InputStream in, IResourceLocator resourceLocator, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        dependencies = new ArrayList<>();
        unmarshaller.setAdapter(new ResourceReferenceXmlAdapter(dependencies, resourceLocator));
    }

    public void beforeWrite(OutputStream out, IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        dependencies = new ArrayList<>();
        marshaller.setAdapter(ResourceReferenceXmlAdapter.class, new ResourceReferenceXmlAdapter(dependencies, resourceLocator));
    }

    /**
     * Override this method if you want to modify the resource after reading it, just
     * before to close the stream.
     *
     * @param inputStream     the open input stream
     * @param resourceLocator the resource locator
     * @param resource        the entity
     * @param unmarshaller    the unmarshaller
     * @param progressMonitor the progress monitor
     * @throws PersistenceException the persistence exception
     */
    public void afterRead(InputStream inputStream, IResourceLocator resourceLocator, R resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    @Override
    protected R readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        R entity;

        try {
            List<Class> classList = new ArrayList<>();
            classList.add(getResourceClass());
            for (IPlugin p : ApplicationContext.getPluginManger().getPlugins()) {
                classList.add(p.getPluginClass());
            }
            JAXBContext context = JAXBContext.newInstance(classList.toArray(new Class[classList.size()]));

            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream in = resourceLocator.openInputStream(progressMonitor);

            beforeRead(in, resourceLocator, unmarshaller, progressMonitor);
            entity = (R) unmarshaller.unmarshal(in);
            afterRead(in, resourceLocator, entity, unmarshaller, progressMonitor);

            in.close();

        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        return entity;
    }

    public void afterWrite(OutputStream out, IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        // Force write the dependencies
        for (ResourceReference<? extends IResource> dependency : dependencies) {

            IResourceLocator dependencyLocator = resourceLocator.getReferenceLocator(dependency.getLocator().getName());

            if (dependency.isChanged()) {

                // Rewrite the resource
                dependency.get();
                ApplicationContext.getPersistenceManager().store(dependencyLocator, dependency.get(), dependency.getResourceFormat(), progressMonitor);

            } else {

                if (!resourceLocator.equals(resource.getLocator())) {

                    // We are in a 'Save as...'
                    String fromName = resource.getLocator().getBaseName();
                    String toName = resourceLocator.getBaseName();

                    String dependencyName = dependency.getLocator().getName().replace(toName, fromName);
                    IResourceLocator fromLocator = resource.getLocator().getReferenceLocator(dependencyName);

                    if (!(fromLocator.isContainer() && dependencyLocator.isContainer())) {

                        File output = dependencyLocator.getWriteFile();

                        // Copy file to file
                        try {

                            if (!output.exists()) {
                                output.createNewFile();
                            }

                            IOUtils.copy(fromLocator.openInputStream(progressMonitor), new FileOutputStream(output));

                            fromLocator.close(progressMonitor);
                            dependencyLocator.close(progressMonitor);

                        } catch (IOException e) {
                            throw new PersistenceException(e);
                        }

                        //TODO Do this properly
                        // Copy the mtabix file if it's present
                        if (dependencyName.endsWith("tdm.gz")) {

                            fromLocator = resource.getLocator().getReferenceLocator(dependencyName + ".mtabix");
                            output = new File(output.getParentFile(), dependency.getLocator().getName() + ".mtabix");

                            try {

                                output.createNewFile();
                                IOUtils.copy(fromLocator.openInputStream(progressMonitor), new FileOutputStream(output));
                                fromLocator.close(progressMonitor);

                            } catch (Exception e) {
                                output.delete();
                            }
                        }
                    }

                }

            }
        }
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor monitor) throws PersistenceException {
        monitor.begin("Saving " + resourceLocator.getName(), 1);

        try {

            List<Class> classList = new ArrayList<>();
            classList.add(getResourceClass());
            for (IPlugin p : ApplicationContext.getPluginManger().getPlugins()) {
                classList.add(p.getPluginClass());
            }
            JAXBContext context = JAXBContext.newInstance(classList.toArray(new Class[classList.size()]));
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Prepare to write
            beforeWrite(null, resourceLocator, resource, marshaller, monitor);

            // Marshal to a temporal file to create dependencies
            File tmpFile = File.createTempFile("gitools-" + resourceLocator.getName(), ".tmp");
            marshaller.marshal(resource, tmpFile);

            // Write the XML
            OutputStream out = resourceLocator.openOutputStream(monitor);
            InputStream in = new FileInputStream(tmpFile);
            org.apache.commons.io.IOUtils.copy(in, out);

            // Write the dependencies
            afterWrite(out, resourceLocator, resource, marshaller, monitor);

            // Close everything
            in.close();
            tmpFile.delete();
            out.close();

        } catch (Exception e) {
            throw new PersistenceException(e);
        }

    }

}
