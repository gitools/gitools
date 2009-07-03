package org.gitools.persistence;

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gitools.model.Artifact;
import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.xml.ResourceXmlAdapter;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class JAXBPersistence implements IEntityPersistence<Artifact> {

	private static final long serialVersionUID = -3625243178449832555L;

	private static final Map<String, Class<?>> map = new HashMap<String, Class<?>>();

	private Class<?> entityClass;
	private final IResource baseResource;

	static {

		map.put(FileExtensions.PROJECT, Project.class);
		map.put(FileExtensions.RESOURCE_CONTAINER, ResourceContainer.class);
		map.put(FileExtensions.MATRIX_FIGURE, MatrixFigure.class);
	}

	public JAXBPersistence(IResource baseResource, String fileExtension) {
		this.baseResource = baseResource;
		entityClass = map.get(fileExtension);
	}

	public JAXBPersistence(IResource baseResource, Class<?> entityClass) {
		this.baseResource = baseResource;
		this.entityClass = entityClass;

	}

	@Override
	public Artifact read(IResource resource, IProgressMonitor monitor)
			throws PersistenceException {

		Artifact entity;
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
			entity = (Artifact) u.unmarshal(reader);
			reader.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
		return entity;
	}

	@Override
	public void write(IResource resource, Artifact entity,
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
			m.marshal(entity, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
}
