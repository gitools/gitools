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
package org.gitools.persistence.formats.analysis;

import org.gitools.persistence.*;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractXmlFormat<R extends IResource> extends AbstractResourceFormat<R>
{
    private List<ResourceReference> dependencies;

    AbstractXmlFormat(String extension, Class<R> resourceClass)
    {
        super(extension, resourceClass);
    }

    @Override
    public boolean isConfigurable()
    {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, @NotNull Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
    }

    void beforeRead(InputStream in, IResourceLocator resourceLocator, @NotNull Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        dependencies = new ArrayList<ResourceReference>();
        unmarshaller.setAdapter(new ResourceReferenceXmlAdapter(dependencies, resourceLocator));

    }

    void beforeWrite(OutputStream out, IResourceLocator resourceLocator, R resource, @NotNull Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        dependencies = new ArrayList<ResourceReference>();
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
    void afterRead(InputStream inputStream, IResourceLocator resourceLocator, R resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
    }

    @Override
    protected R readResource(@NotNull IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException
    {
        R entity;

        try
        {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Unmarshaller unmarshaller = context.createUnmarshaller();


            InputStream in = resourceLocator.openInputStream();

            beforeRead(in, resourceLocator, unmarshaller, progressMonitor);
            entity = (R) unmarshaller.unmarshal(in);
            afterRead(in, resourceLocator, entity, unmarshaller, progressMonitor);

            in.close();

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new PersistenceException(e);
        }

        return entity;
    }

    void afterWrite(OutputStream out, IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        // Force write the dependencies
        for (ResourceReference dependency : dependencies)
        {
            IResourceLocator dependencyLocator = dependency.getLocator();
            PersistenceManager.get().store(dependencyLocator, dependency.get(), progressMonitor);
        }
    }

    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, R resource, @NotNull IProgressMonitor monitor) throws PersistenceException
    {
        monitor.begin("Saving " + resourceLocator.getName(), 1);

        try
        {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            OutputStream out = resourceLocator.openOutputStream();
            beforeWrite(out, resourceLocator, resource, marshaller, monitor);
            marshaller.marshal(resource, out);
            afterWrite(out, resourceLocator, resource, marshaller, monitor);
            out.close();

        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

}
