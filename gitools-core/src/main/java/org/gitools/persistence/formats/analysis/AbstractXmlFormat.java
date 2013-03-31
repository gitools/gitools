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

package org.gitools.persistence.formats.analysis;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public abstract class AbstractXmlFormat<R extends IResource> extends AbstractResourceFormat<R> {

    public static final String LOAD_REFERENCES_PROP = "load_references";

    private boolean loadReferences;

    public AbstractXmlFormat(String extension, String mime, Class<R> resourceClass) {
        super(extension, mime, resourceClass);
    }

    protected boolean isLoadReferences() {
        return loadReferences;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        this.loadReferences = Boolean.parseBoolean(properties.getProperty(LOAD_REFERENCES_PROP, "true"));
    }

    protected void beforeRead(IResourceLocator resourceLocator, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        unmarshaller.setAdapter(new PersistenceReferenceXmlAdapter(resourceLocator, progressMonitor, loadReferences));
    }

    protected void afterRead(IResourceLocator resourceLocator, R entity, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    @Override
    protected R readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        R entity;

        try {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Unmarshaller unmarshaller = context.createUnmarshaller();

            beforeRead(resourceLocator, unmarshaller, progressMonitor);

            InputStream in = resourceLocator.openInputStream();
            entity = (R) unmarshaller.unmarshal(in);
            in.close();

            afterRead(resourceLocator, entity, unmarshaller, progressMonitor);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException(e);
        }

        return entity;
    }

    protected void beforeWrite(IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    protected void afterWrite(IResourceLocator resourceLocator, R resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, R resource, IProgressMonitor monitor) throws PersistenceException {
        monitor.begin("Saving " + resourceLocator.getName(), 1);

        try {
            JAXBContext context = JAXBContext.newInstance(getResourceClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            beforeWrite(resourceLocator, resource, marshaller, monitor);

            OutputStream out = resourceLocator.openOutputStream();
            marshaller.marshal(resource, out);
            out.close();

            afterWrite(resourceLocator, resource, marshaller, monitor);

        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

    @Deprecated
    protected static void addReference(PersistenceReferenceXmlAdapter adapter, IResource resource, String fieldName) {
        String defaultExtension = PersistenceManager.get().getDefaultExtension(resource);
        adapter.addReference(resource, fieldName + "." + defaultExtension + ".gz");
    }

}
