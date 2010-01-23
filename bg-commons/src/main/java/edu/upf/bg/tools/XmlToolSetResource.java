package edu.upf.bg.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.upf.bg.tools.exception.ToolException;

public class XmlToolSetResource {

	protected File file;
	
	public XmlToolSetResource(File file) {
		this.file = file;
	}
	
	public ToolSet load() throws ToolException {
		try {
			return load(new FileReader(file));
		} catch (Exception e) {
			throw new ToolException(e);
		}
	}
	
	public void save(ToolSet toolSet) throws ToolException {
		try {
			save(toolSet, new FileWriter(file));
		} catch (Exception e) {
			throw new ToolException(e);
		}
	}
	
	public static ToolSet load(Reader reader) throws ToolException {
		ToolSet toolSet = null;
		try {
			JAXBContext context = JAXBContext.newInstance(
					ToolSet.class, ToolDescriptor.class, ArrayList.class);
			
			Unmarshaller u = context.createUnmarshaller();
			toolSet = (ToolSet) u.unmarshal(reader);
		}
		catch (Exception e) {
			throw new ToolException(e);
		}
		return toolSet;
	}
	
	public static void save(ToolSet toolSet, Writer writer) throws ToolException {
		try {
			JAXBContext context = 
				JAXBContext.newInstance(ToolSet.class);
			
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			m.marshal(toolSet, writer);
		}
		catch (Exception e) {
			throw new ToolException(e);
		}
	}
}
