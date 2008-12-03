package es.imim.bg.ztools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Investigation;

public class InvestigationResource extends Resource {

	public InvestigationResource(String resource) {
		super(resource);
	}
	
	public InvestigationResource(File file) {
		super(file);
	}
	
	public void save(Investigation inv, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, JAXBException {
		
		Writer writer = openWriter();
		
		JAXBContext context = JAXBContext.newInstance(Investigation.class);
		Marshaller m = context.createMarshaller();
		m.marshal(inv, writer);
		
		writer.close();
	}
}
