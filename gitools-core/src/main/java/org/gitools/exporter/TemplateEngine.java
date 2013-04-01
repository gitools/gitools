/*
 * #%L
 * gitools-core
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
package org.gitools.exporter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.gitools.utils.fileutils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class TemplateEngine
{

    private VelocityEngine velocityEngine;
    private String templateName;
    private Template template;
    private VelocityContext context;

    public TemplateEngine(Properties props)
    {
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

    public TemplateEngine()
    {
        this(new Properties());
    }

    public void setFileLoaderPath(File file)
    {
        velocityEngine.setProperty(
                VelocityEngine.FILE_RESOURCE_LOADER_PATH,
                Arrays.asList(file.getAbsolutePath()));
    }

    public void init()
    {
        try
        {
            velocityEngine.init();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void loadTemplate(String name)
            throws ResourceNotFoundException, ParseErrorException, Exception
    {

        if (template == null || !this.templateName.equals(name))
        {
            template = velocityEngine.getTemplate(name);
            this.templateName = name;
        }
    }

    public void setContext(Map<?, ?> context)
    {
        this.context = new VelocityContext(context);
    }

    public void render(Writer writer)
            throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException
    {

        template.merge(context, writer);
    }

    public void render(File file)
            throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException
    {

        Writer writer = IOUtils.openWriter(file);
        render(writer);
        writer.close();
    }
}
