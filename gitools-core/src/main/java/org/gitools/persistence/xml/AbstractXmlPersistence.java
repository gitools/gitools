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

package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.persistence.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public abstract class AbstractXmlPersistence<R> extends AbstractResourcePersistence<R> {

    public static final String LOAD_REFERENCES_PROP = "load_references";

    private Class<R> entityClass;

    private XmlAdapter<?, ?>[] adapters;

    private String persistenceTitle;

    private PersistenceContext persistenceContext;

    public AbstractXmlPersistence(Class<R> entityClass) {
        this.entityClass = entityClass;
        this.persistenceTitle = entityClass.getSimpleName();
        this.persistenceContext = new PersistenceContext();
    }

    public void setAdapters(XmlAdapter<?, ?>[] adapters) {
        this.adapters = adapters;
    }

    public XmlAdapter<?, ?>[] getAdapters() {
        return adapters;
    }

    public PersistenceContext getPersistenceContext() {
        return persistenceContext;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String lr = properties.getProperty(LOAD_REFERENCES_PROP, "true");
        boolean loadReferences = lr == null || Boolean.parseBoolean(lr);
        persistenceContext.setLoadReferences(loadReferences);
    }

    /**
     * Classes extending AbstractXmlPersistence should
     * override this method if they need to specify adapters.
     */
    protected XmlAdapter<?, ?>[] createAdapters() {
        return new XmlAdapter<?, ?>[0];
    }

    public void setPersistenceTitle(String entityName) {
        this.persistenceTitle = entityName;
    }

    protected void beforeRead(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    protected void afterRead(IResourceLocator resourceLocator, R entity, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    @Override
    public R read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        R entity;
        Reader reader;

        beforeRead(resourceLocator, progressMonitor);

        try {
            reader = resourceLocator.getReader();
            JAXBContext context = JAXBContext.newInstance(entityClass);
            Unmarshaller u = context.createUnmarshaller();

            if (adapters == null)
                setAdapters(createAdapters());

            for (XmlAdapter<?, ?> adapter : adapters)
                u.setAdapter(adapter);

            entity = (R) u.unmarshal(reader);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Error reading: " + resourceLocator.getURL(), e);
        }

        afterRead(resourceLocator, entity, progressMonitor);

        return entity;
    }

    protected void beforeWrite(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    protected void afterWrite(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException {
    }

    @Override
    public void write(IResourceLocator resourceLocator, R resource, IProgressMonitor monitor) throws PersistenceException {

        Writer writer;

        monitor.begin("Saving " + persistenceTitle + "...", 1);
        monitor.info("To: " + resourceLocator.getURL());

        beforeWrite(resourceLocator, resource, monitor.subtask());

        try {
            writer = resourceLocator.getWriter();
            JAXBContext context = JAXBContext.newInstance(entityClass);
            Marshaller m = context.createMarshaller();

            if (adapters == null)
                setAdapters(createAdapters());

            for (XmlAdapter<?, ?> adapter : adapters)
                m.setAdapter(adapter);

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(resource, writer);
            writer.close();

        } catch (Exception e) {
            throw new PersistenceException("Error writing: " + resourceLocator.getURL(), e);
        }

        afterWrite(resourceLocator, resource, monitor.subtask());

        monitor.end();
    }
}
