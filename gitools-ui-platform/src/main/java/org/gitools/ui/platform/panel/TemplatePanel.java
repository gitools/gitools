package org.gitools.ui.platform.panel;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplatePanel extends Html4Panel {

	private static final long serialVersionUID = 1939265225161205798L;
	
	private VelocityEngine velocityEngine;
	private String templateName;
	private Template template;
	private VelocityContext context;

	public TemplatePanel(Properties props) {
		velocityEngine = new VelocityEngine();
		
		velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		velocityEngine.setProperty(
				"class." + VelocityEngine.RESOURCE_LOADER + ".class", 
				ClasspathResourceLoader.class.getName());
		
		velocityEngine.setProperty(VelocityEngine.COUNTER_NAME, "forIndex");
		velocityEngine.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");

		// TODO runtime.log.logsystem.class <-> org.apache.velocity.runtime.log.LogChute
		
		velocityEngine.setProperty("runtime.log.logsystem.log4j.logger",
				"org.apache.velocity.runtime.log.Log4JLogChute" );
		
		velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		
		//FIXME: external parameter
		// velocityEngine.setProperty(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");

		for (Entry<Object, Object> prop : props.entrySet())
			velocityEngine.setProperty(
					(String) prop.getKey(), prop.getValue());
		
		try {
			velocityEngine.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TemplatePanel() {
		this(new Properties());
	}

	public Template getTemplate() {
		return template;
	}
	
	public void setTemplate(String name) 
			throws ResourceNotFoundException, ParseErrorException, Exception {
		
		if (template == null || !this.templateName.equals(name)) {
			template = velocityEngine.getTemplate(name);
			this.templateName = name;
		}
	}
	
	public VelocityContext getContext() {
		return context;
	}
	
	public void setContext(VelocityContext context) {
		this.context = context;
	}
	
	public void render() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		final StringWriter sw = new StringWriter();
		template.merge(context, sw);

		URL url = getClass().getResource("/vm/analysis/combination.vm");
		System.out.println(sw.toString());
		panel.setHtml(sw.toString(), url.toString(), rcontext);
	}
}
