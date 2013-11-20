/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.platform.panel;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @noinspection ALL
 */
public class TemplatePanel extends Html4Panel {

    private static final long serialVersionUID = 1939265225161205798L;

    private final VelocityEngine velocityEngine;
    private String templateName;
    private String templateUrl;
    private Template template;
    private VelocityContext context;

    private TemplatePanel(Properties props) {
        super();

        velocityEngine = new VelocityEngine();

        velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityEngine.setProperty("class." + VelocityEngine.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());

        velocityEngine.setProperty(VelocityEngine.COUNTER_NAME, "forIndex");
        velocityEngine.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");

        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity.runtime.log.Log4JLogChute");

        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");

        for (Entry<Object, Object> prop : props.entrySet())
            velocityEngine.setProperty((String) prop.getKey(), prop.getValue());

        try {
            velocityEngine.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TemplatePanel() {
        this(new Properties());
    }

    @Deprecated
    public void setTemplateFromResource(String resource) throws Exception {

        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }

        setTemplateFromResource(resource, "http://localhost");
    }

    void setTemplateFromResource(String resource, String baseUrl) throws Exception {

        setTemplateFromResource(resource, new URL(baseUrl));
    }

    public void setTemplateFromResource(String resource, URL baseUrl) throws Exception {
        if (template == null || !this.templateName.equals(resource)) {
            template = velocityEngine.getTemplate(resource);
            this.templateName = resource;
        }

        this.templateUrl = baseUrl.toString();
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
