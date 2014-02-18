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

import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractXmlFormat<R extends IResource> extends AbstractResourceFormat<R> {
    private List<ResourceReference> dependencies;

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
        marshaller.setAdapter(new ResourceReferenceXmlAdapter(dependencies, resourceLocator));
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
            JAXBContext context = JAXBContext.newInstance(getResourceClass());

            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream in = resourceLocator.openInputStream(progressMonitor);

            beforeRead(in, resourceLocator, unmarshaller, progressMonitor);
            entity = (R) unmarshaller.unmarshal(in);
            afterRead(in, resourceLocator, entity, unmarshaller, progressMonitor);

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException(e);
        }

        return entity;
    }

    public void afterWrite(OutputStream out, IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        // Force write the dependencies
        for (ResourceReference dependency : dependencies) {
            IResourceLocator dependencyLocator = dependency.getLocator();
            ApplicationContext.getPersistenceManager().store(dependencyLocator, dependency.get(), dependency.getResourceFormat(), progressMonitor);
        }
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor monitor) throws PersistenceException {
        monitor.begin("Saving " + resourceLocator.getName(), 1);

        try {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            OutputStream out = resourceLocator.openOutputStream();
            beforeWrite(out, resourceLocator, resource, marshaller, monitor);
            marshaller.marshal(resource, out);
            afterWrite(out, resourceLocator, resource, marshaller, monitor);
            out.close();

        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

}
