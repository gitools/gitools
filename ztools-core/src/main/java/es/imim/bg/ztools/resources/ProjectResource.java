package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Project;

public class ProjectResource extends Resource {

	public ProjectResource(String resource) {
		super(resource);
	}
	
	public ProjectResource(File file) {
		super(file);
	}
	
	public void save(Project inv, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		Writer writer = openWriter();
		
		JAXBContext context = JAXBContext.newInstance(Project.class);
		Marshaller m = context.createMarshaller();
		m.marshal(inv, writer);
		
		writer.close();
	}
}
