package org.gitools.persistence;

import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gitools.model.Project;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ProjectPersistence implements IEntityPersistence<Project> {

	private static final long serialVersionUID = -3625243178449832555L;

	@Override
	public Project read(IResource resource, IProgressMonitor monitor)
			throws PersistenceException {

		Project project;

		Reader reader;

		try {
			reader = resource.openReader();

		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
		}

		JAXBContext context;
		
		try {

			context = JAXBContext.newInstance(Project.class);
			Unmarshaller u = context.createUnmarshaller();
			project = (Project) u.unmarshal(reader);

			reader.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}

		return project;
	}

	@Override
	public void write(IResource resource, Project project,
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

			context = JAXBContext.newInstance(Project.class);
			Marshaller m = context.createMarshaller();
			m.marshal(project, writer);
			writer.close();

		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
}
