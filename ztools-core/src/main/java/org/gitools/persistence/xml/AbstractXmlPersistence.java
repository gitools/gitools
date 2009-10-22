package org.gitools.persistence.xml;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.persistence.AbstractEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public abstract class AbstractXmlPersistence<T> extends AbstractEntityPersistence<T> {

	private static final long serialVersionUID = -3625243178449832555L;
	
	private Class<T> entityClass;
	
	private XmlAdapter<?, ?>[] adapters;
	
	public AbstractXmlPersistence(Class<T> entityClass) {	
		this.entityClass = entityClass;
	}

	public void setAdapters(XmlAdapter<?, ?>[] adapters) {
		this.adapters = adapters;
	}
	
	public XmlAdapter<?, ?>[] getAdapters() {
		return adapters;
	}
	
	/** Classes extending AbstractXmlPersistence should
	 * override this method if they need to specify adapters. */
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter<?, ?>[0];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T read(
			File file,
			IProgressMonitor monitor)
			throws PersistenceException {

		T entity;
		Reader reader;

		try {
			reader = PersistenceUtils.openReader(file);
			JAXBContext context = JAXBContext.newInstance(entityClass);
			Unmarshaller u = context.createUnmarshaller();
			
			if (adapters == null)
				setAdapters(createAdapters());
			
			for (XmlAdapter<?, ?> adapter : adapters)
				u.setAdapter(adapter);

			entity = (T) u.unmarshal(reader);
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}
		
		return entity;
	}

	@Override
	public void write(
			File file,
			T entity,
			IProgressMonitor monitor)
			throws PersistenceException {

		Writer writer;
		
		try {
			writer = PersistenceUtils.openWriter(file);
			JAXBContext context = JAXBContext.newInstance(entityClass);
			Marshaller m = context.createMarshaller();
		
			if (adapters == null)
				setAdapters(createAdapters());
			
			for (XmlAdapter<?, ?> adapter : adapters)
				m.setAdapter(adapter);

			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: " + file.getName(), e);
		}
	}
}
