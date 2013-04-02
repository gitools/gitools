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

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public abstract class AbstractXmlFormat<R extends IResource> extends AbstractResourceFormat<R>
{

    public static final String LOAD_REFERENCES_PROP = "load_references";

    private boolean loadReferences;

    public AbstractXmlFormat(String extension, String mime, Class<R> resourceClass)
    {
        super(extension, mime, resourceClass);
    }

    protected boolean isLoadReferences()
    {
        return loadReferences;
    }

    @Override
    public boolean isConfigurable()
    {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        this.loadReferences = Boolean.parseBoolean(properties.getProperty(LOAD_REFERENCES_PROP, "true"));
    }

    protected void beforeRead(IResourceLocator resourceLocator, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        unmarshaller.setAdapter(new PersistenceReferenceXmlAdapter(resourceLocator, progressMonitor, loadReferences));
        unmarshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }

    /**
     * Override this method if you want to modify the resource after reading it.
     *
     * @param resourceLocator the resource locator
     * @param resource the entity
     * @param unmarshaller the unmarshaller
     * @param progressMonitor the progress monitor
     * @throws PersistenceException the persistence exception
     */
    protected void afterRead(IResourceLocator resourceLocator, R resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
    }

    @Override
    protected R readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException
    {
        R entity;

        try
        {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Unmarshaller unmarshaller = context.createUnmarshaller();

            beforeRead(resourceLocator, unmarshaller, progressMonitor);

            InputStream in = resourceLocator.openInputStream();
            entity = (R) unmarshaller.unmarshal(in);
            in.close();

            afterRead(resourceLocator, entity, unmarshaller, progressMonitor);

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new PersistenceException(e);
        }

        return entity;
    }

    protected void beforeWrite(IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        marshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }

    protected void afterWrite(IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor monitor) throws PersistenceException
    {
        monitor.begin("Saving " + resourceLocator.getName(), 1);

        try
        {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            beforeWrite(resourceLocator, resource, marshaller, monitor);

            OutputStream out = resourceLocator.openOutputStream();
            marshaller.marshal(resource, out);
            out.close();

            afterWrite(resourceLocator, resource, marshaller, monitor);

        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

    @Deprecated
    protected static void addReference(PersistenceReferenceXmlAdapter adapter, IResource resource, String fieldName)
    {
        String defaultExtension = PersistenceManager.get().getDefaultExtension(resource);
        adapter.addReference(resource, fieldName + "." + defaultExtension + ".gz");
    }

}
