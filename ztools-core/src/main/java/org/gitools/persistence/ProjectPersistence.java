package org.gitools.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gitools.model.Project;
import org.gitools.model.analysis.Analysis;
import org.gitools.resources.FileResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ProjectPersistence extends FileResource {

	private static final long serialVersionUID = -3625243178449832555L;
	
	/*public ProjectResource(String resource) {
		super(resource);
	}*/
	
	protected String fileName;
	
	public ProjectPersistence(File basePath, String fileName) {
		super(basePath);
		
		this.fileName = fileName;
	}

	public ProjectPersistence(File basePath) {
		this(basePath, "project.xml");
	}

	public void save(Project project, IProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		final File basePath = getFile();
		
		Writer writer = openWriter(new File(basePath, fileName));
		
		JAXBContext context = JAXBContext.newInstance(Project.class);
		
		Marshaller m = context.createMarshaller();
		m.marshal(project, writer);
		
		writer.close();
		
		for (Analysis analysis : project.getAnalysis()) {
			new TextObjectMatrixPersistence(basePath)
				.write(analysis.getResults(), analysis.getName(), monitor);
		}
	}
	
	public Project load(IProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		Project project = null;
		
		final File basePath = getFile();
		
		Reader reader = openReader(new File(basePath, fileName));
		
		JAXBContext context = JAXBContext.newInstance(Project.class);
		
		Unmarshaller u = context.createUnmarshaller();
		project = (Project) u.unmarshal(reader);
		
		reader.close();
		
		
		
		return project;
	}
}
