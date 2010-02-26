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
import org.gitools.persistence.PersistenceContext;

public abstract class AbstractXmlPersistence<T> extends AbstractEntityPersistence<T> {

	private static final long serialVersionUID = -3625243178449832555L;
	
	private Class<T> entityClass;

	private XmlAdapter<?, ?>[] adapters;

	private boolean recursivePersistence;

	private String persistenceTitle;

	private PersistenceContext persistenceContext;
	
	public AbstractXmlPersistence(Class<T> entityClass) {	
		this.entityClass = entityClass;
		this.persistenceTitle = entityClass.getSimpleName();
		this.recursivePersistence = true;
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

	public void setPersistenceContext(PersistenceContext persistenceContext) {
		this.persistenceContext = persistenceContext;
	}
	
	/** Classes extending AbstractXmlPersistence should
	 * override this method if they need to specify adapters. */
	protected XmlAdapter<?, ?>[] createAdapters() {
		return new XmlAdapter<?, ?>[0];
	}

	public boolean isRecursivePersistence() {
		return recursivePersistence;
	}

	public void setRecursivePersistence(boolean recirsivePersistence) {
		this.recursivePersistence = recirsivePersistence;
	}
	
	public String getPersistenceTitle() {
		return persistenceTitle;
	}

	public void setPersistenceTitle(String entityName) {
		this.persistenceTitle = entityName;
	}

	protected void beforeRead(File file, IProgressMonitor monitor) throws PersistenceException {}
	protected void afterRead(File file, T entity, IProgressMonitor monitor) throws PersistenceException {}

	@SuppressWarnings("unchecked")
	@Override
	public T read(
			File file,
			IProgressMonitor monitor)
			throws PersistenceException {

		T entity;
		Reader reader;

		beforeRead(file, monitor);

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
			throw new PersistenceException("Error reading resource: " + file.getName(), e);
		}

		afterRead(file, entity, monitor);
		
		return entity;
	}

	protected void beforeWrite(File file, T entity, IProgressMonitor monitor) throws PersistenceException {}
	protected void afterWrite(File file, T entity, IProgressMonitor monitor) throws PersistenceException {}

	@Override
	public void write(
			File file,
			T entity,
			IProgressMonitor monitor)
			throws PersistenceException {

		Writer writer;

		monitor.begin("Saving " + persistenceTitle + "...", 1);
		monitor.info("File: " + file.getAbsolutePath());

		beforeWrite(file, entity, monitor.subtask());

		try {
			writer = PersistenceUtils.openWriter(file);
			JAXBContext context = JAXBContext.newInstance(entityClass);
			Marshaller m = context.createMarshaller();
		
			if (adapters == null)
				setAdapters(createAdapters());
			
			for (XmlAdapter<?, ?> adapter : adapters)
				m.setAdapter(adapter);
			
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException("Error writing resource: " + file.getName(), e);
		}

		afterWrite(file, entity, monitor.subtask());

		monitor.end();
	}
}
