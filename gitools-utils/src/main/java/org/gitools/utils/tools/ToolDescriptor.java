/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.tools;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * It represents the descriptor for a tool
 * and contains all the relevant information for the tool.
 */

@XmlRootElement
public class ToolDescriptor
{

    protected String name;
    protected String description;
    protected Class<?> argsClass;
    protected Class<? extends ToolLifeCycle> lifeCycleClass;

    public ToolDescriptor()
    {
    }

    public ToolDescriptor(
            String name,
            String descr,
            Class<?> argsObject,
            Class<? extends ToolLifeCycle> toolClass)
    {

        this.name = name;
        this.description = descr;
        this.argsClass = argsObject;
        this.lifeCycleClass = toolClass;
    }

    @XmlElement
    public String getName()
    {
        return name;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    @XmlElement
    public String getDescription()
    {
        return description;
    }

    protected void setDescription(String description)
    {
        this.description = description;
    }

    @XmlElement
    public Class<?> getArgumentsClass()
    {
        return argsClass;
    }

    protected void setArgumentsClass(Class<?> argsClass)
    {
        this.argsClass = argsClass;
    }

    @XmlElement
    public Class<? extends ToolLifeCycle> getLifeCycleClass()
    {
        return lifeCycleClass;
    }

    protected void setLifeCycleClass(Class<? extends ToolLifeCycle> lifeCycleClass)
    {
        this.lifeCycleClass = lifeCycleClass;
    }
}
