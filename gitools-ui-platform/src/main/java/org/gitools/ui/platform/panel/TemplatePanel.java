/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.panel;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
	private String templateUrl;
	private Template template;
	private VelocityContext context;

	public TemplatePanel(Properties props) {
        super();

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
	
	@Deprecated // specify a base url
	public void setTemplateFromResource(String resource)
			throws ResourceNotFoundException, ParseErrorException, Exception {

		if (!resource.startsWith("/"))
			resource = "/" + resource;
		
		//setTemplateFromResource(new URL("http://localhost" + path));
		setTemplateFromResource(resource, "http://localhost");
	}

	//@Deprecated // use setTemplateFromResource(URL url)
	public void setTemplateFromResource(String resource, String baseUrl) throws Exception {
		
		setTemplateFromResource(resource, new URL(baseUrl));
	}

	public void setTemplateFromResource(String resource, URL baseUrl) throws Exception {
		if (template == null || !this.templateName.equals(resource)) {
			template = velocityEngine.getTemplate(resource);
			this.templateName = resource;
		}

		this.templateUrl = baseUrl.toString();
	}

	/*public void setTemplateFromResource(URL url) throws Exception {
		String basePath = getClass().getResource("/").getPath();
		String path = url.getPath();

		if (template == null || !this.templateName.equals(path)) {
			template = velocityEngine.getTemplate(path);
			this.templateName = path;
		}

		this.templateUrl = url.toString();
	}*/

	public String getTemplateUrl() {
		return templateUrl;
	}

	public VelocityContext getContext() {
		return context;
	}
	
	public void setContext(VelocityContext context) {
		this.context = context;
	}

	public void render(VelocityContext context) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		final StringWriter sw = new StringWriter();
		template.merge(context, sw);

        panel.setHtml(sw.toString(), templateUrl, rcontext);
	}

	public void render() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		render(context);
	}

	public void merge(VelocityContext context, Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		template.merge(context, writer);
	}
}
