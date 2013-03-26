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

package org.gitools.exporter;

import edu.upf.bg.fileutils.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class TemplateEngine {

	private VelocityEngine velocityEngine;
	private String templateName;
	private Template template;
	private VelocityContext context;
	
	public TemplateEngine(Properties props) {
		velocityEngine = new VelocityEngine();
		
		velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "file, class");
		velocityEngine.setProperty(
				"class." + VelocityEngine.RESOURCE_LOADER + ".class", 
				ClasspathResourceLoader.class.getName());
		
		velocityEngine.setProperty(VelocityEngine.COUNTER_NAME, "forIndex");
		velocityEngine.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");
		
		for (Entry<Object, Object> prop : props.entrySet())
			velocityEngine.setProperty(
					(String) prop.getKey(), prop.getValue());
		
		context = new VelocityContext();
	}
	
	public TemplateEngine() {
		this(new Properties());
	}
	
	public void setFileLoaderPath(File file) {
		velocityEngine.setProperty(
				VelocityEngine.FILE_RESOURCE_LOADER_PATH, 
				Arrays.asList(file.getAbsolutePath()));
	}
	
	public void init() {
		try {
			velocityEngine.init();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
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
		
		Writer writer = IOUtils.openWriter(file);
		render(writer);
		writer.close();
	}
}
