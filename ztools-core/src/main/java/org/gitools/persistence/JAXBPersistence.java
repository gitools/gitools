package org.gitools.persistence;

import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gitools.model.xml.MatrixXmlAdapter;
import org.gitools.model.xml.ResourceXmlAdapter;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class JAXBPersistence implements IEntityPersistence<Object> {

	private static final long serialVersionUID = -3625243178449832555L;

	private final IResource baseResource;
	private Class<?> entityClass;

	public JAXBPersistence(IResource baseResource, String fileExtension) {
		this.baseResource = baseResource;
		entityClass = FileExtensions.getEntityClass(fileExtension);
	}

	public JAXBPersistence(IResource baseResource, Class<?> entityClass) {
		this.baseResource = baseResource;
		this.entityClass = entityClass;

	}

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

		JAXBContext context;
		try {

			context = JAXBContext.newInstance(entityClass);
			Unmarshaller u = context.createUnmarshaller();

			u.setAdapter(new ResourceXmlAdapter(this.baseResource));
			u.setAdapter(new MatrixXmlAdapter((FileResource) resource));
		
			entity = (Object) u.unmarshal(reader);
			reader.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
		return entity;
	}

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

		JAXBContext context;

		try {

			context = JAXBContext.newInstance(entityClass);
			Marshaller m = context.createMarshaller();

			m.setAdapter(new ResourceXmlAdapter(this.baseResource));
			m.setAdapter(new MatrixXmlAdapter((FileResource) resource));

			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
}
