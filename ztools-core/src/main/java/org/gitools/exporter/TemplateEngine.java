package org.gitools.exporter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.gitools.resources.FileResource;

public class TemplateEngine {

	private VelocityEngine velocityEngine;
	private String templateName;
	private Template template;
	private VelocityContext context;
	
	public TemplateEngine(Properties props) {
		velocityEngine = new VelocityEngine();
		
		velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		velocityEngine.setProperty(
				"class." + VelocityEngine.RESOURCE_LOADER + ".class", 
				ClasspathResourceLoader.class.getName());
		
		velocityEngine.setProperty(VelocityEngine.COUNTER_NAME, "forIndex");
		velocityEngine.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");
		
		for (Entry<Object, Object> prop : props.entrySet())
			velocityEngine.setProperty(
					(String) prop.getKey(), prop.getValue());
		
		try {
			velocityEngine.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		context = new VelocityContext();
	}
	
	public TemplateEngine() {
		this(new Properties());
	}
	
	public void loadTemplate(String name)
			throws ResourceNotFoundException, ParseErrorException, Exception {

		if (template == null || !this.templateName.equals(name)) {
			template = velocityEngine.getTemplate(name);
			this.templateName = name;
		}
	}
	
	public void setContext(Map<?, ?> context) {
		this.context = new VelocityContext(context);
	}
	
	public void render(Writer writer) 
			throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		
		template.merge(context, writer);
	}
	
	public void render(File file) 
		throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		
		Writer writer = FileResource.openWriter(file);
		render(writer);
		writer.close();
	}
}
