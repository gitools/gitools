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
package org.gitools.matrix.model.element;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@XmlRootElement
@XmlSeeAlso(value = {
        BeanElementProperty.class})

public abstract class AbstractElementAttribute
        implements IElementAttribute, Serializable
{

    private static final long serialVersionUID = -6293895024608178858L;

    protected String id;
    protected String name;
    protected String description;
    protected Class<?> valueClass;

    public AbstractElementAttribute()
    {
    }

    public AbstractElementAttribute(
            String id, String name, String description,
            Class<?> valueClass)
    {

        this.id = id;
        this.name = name;
        this.description = description;
        this.valueClass = valueClass;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public Class<?> getValueClass()
    {
        return valueClass;
    }

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        if (name != null)
        {
            sb.append(" : ").append(name);
        }
        if (valueClass != null)
        {
            sb.append(" : ").append(valueClass.getSimpleName());
        }
        if (description != null)
        {
            sb.append(" : ").append(description);
        }
        return sb.toString();
    }
}
