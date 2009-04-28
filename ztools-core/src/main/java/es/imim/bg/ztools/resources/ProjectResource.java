package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Project;

public class ProjectResource extends Resource {

	private static final long serialVersionUID = -3625243178449832555L;
	
	/*public ProjectResource(String resource) {
		super(resource);
	}*/
	
	protected String fileName;
	
	public ProjectResource(File basePath, String fileName) {
		super(basePath);
		
		this.fileName = fileName;
	}

	public ProjectResource(File basePath) {
		this(basePath, "project.xml");
	}

	public void save(Project project, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		final File basePath = getResourceFile();
		
		Writer writer = openWriter(new File(basePath, fileName));
		
		JAXBContext context = JAXBContext.newInstance(Project.class);
		
		Marshaller m = context.createMarshaller();
		m.marshal(project, writer);
		
		writer.close();
		
		for (Analysis analysis : project.getAnalysis()) {
			new ResultsResource(basePath)
				.write(analysis.getResults(), analysis.getName(), monitor);
		}
	}
	
	public Project load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		Project project = null;
		
		final File basePath = getResourceFile();
		
		Reader reader = openReader(new File(basePath, fileName));
		
		JAXBContext context = JAXBContext.newInstance(Project.class);
		
		Unmarshaller u = context.createUnmarshaller();
		project = (Project) u.unmarshal(reader);
		
		reader.close();
		
		
		
		return project;
	}
}
