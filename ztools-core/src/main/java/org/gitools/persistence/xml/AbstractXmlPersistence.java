package org.gitools.persistence.xml;

import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.persistence.IEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.resources.IResource;
import org.gitools.resources.factory.ResourceFactory;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class AbstractXmlPersistence implements IEntityPersistence<Object> {

	private static final long serialVersionUID = -3625243178449832555L;
	private Class<?> entityClass;

	@SuppressWarnings("unchecked")
	protected XmlAdapter[] adapters;

	public AbstractXmlPersistence(Class<?> entityClass) {
		
		this.entityClass = entityClass;
		
		setAdapters(new XmlAdapter[] {
			new ResourceXmlAdapter(new ResourceFactory())
		});
	}

	@SuppressWarnings("unchecked")
	public void setAdapters(XmlAdapter[] adapters) {
		this.adapters = adapters;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object read(IResource resource, IProgressMonitor monitor)
			throws PersistenceException {

		Object entity;
		Reader reader;

		try {
			reader = resource.openReader();
			JAXBContext context = JAXBContext.newInstance(entityClass);
			Unmarshaller u = context.createUnmarshaller();
			for (XmlAdapter adapter : adapters)
				u.setAdapter(adapter);

			entity = (Object) u.unmarshal(reader);
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(IResource resource, Object entity,
			IProgressMonitor monitor) throws PersistenceException {

		Writer writer;
		try {
			writer = resource.openWriter();
			JAXBContext context = JAXBContext.newInstance(entityClass);
			Marshaller m = context.createMarshaller();
			for (XmlAdapter adapter : adapters)
				m.setAdapter(adapter);

			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
		}
	}
}
