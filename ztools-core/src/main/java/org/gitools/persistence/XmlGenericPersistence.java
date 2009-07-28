package org.gitools.persistence;

import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class XmlGenericPersistence implements IEntityPersistence<Object> {

	private static final long serialVersionUID = -3625243178449832555L;
	private JAXBContext context;

	@SuppressWarnings("unchecked")
	protected XmlAdapter[] adapters;
	
	public XmlGenericPersistence(Class<?> entityClass) throws JAXBException {
		adapters = new XmlAdapter[0];
		context = JAXBContext.newInstance(entityClass);
		System.out.println("entitiiiiii ! + " + entityClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object read(IResource resource, IProgressMonitor monitor)
			throws PersistenceException {

		Object entity;
		Reader reader;

		try {
			reader = resource.openReader();
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
		}

		try {

			Unmarshaller u = context.createUnmarshaller();
			for (XmlAdapter adapter : adapters) {
				u.setAdapter(adapter);
			}

	
			entity = (Object) u.unmarshal(reader);
			reader.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
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
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
		}

		try {
			Marshaller m = context.createMarshaller();

			for (XmlAdapter adapter : adapters) {
				m.setAdapter(adapter);
			}

	
			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
}
