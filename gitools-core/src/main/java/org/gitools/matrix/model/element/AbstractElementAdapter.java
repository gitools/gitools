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
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.*;

/**
 * @noinspection ALL
 */
@XmlRootElement
@XmlSeeAlso(value = {BeanElementProperty.class})

public abstract class AbstractElementAdapter implements IElementAdapter, Serializable
{

    private static final long serialVersionUID = -4797939915206004479L;

    Class<?> elementClass;

    @NotNull
    private /*transient*/ List<IElementAttribute> properties = new ArrayList<IElementAttribute>(0);

    private /*transient*/ Map<String, Integer> propIdToIndexMap;

    AbstractElementAdapter()
    {
    }

    AbstractElementAdapter(Class<?> elementClass)
    {
        this.elementClass = elementClass;
    }

    @XmlElement
    @Override
    public Class<?> getElementClass()
    {
        return elementClass;
    }

    void setElementClass(Class<?> elementClass)
    {
        this.elementClass = elementClass;
    }

    @Override
    public final int getPropertyCount()
    {
        return properties.size();
    }

    @Override
    public final IElementAttribute getProperty(int index)
    {
        return properties.get(index);
    }

    //@XmlElement(name = "Property", type=ElementProperty.class)
    @Override
    public final List<IElementAttribute> getProperties()
    {
        return Collections.unmodifiableList(properties);
    }

    final void setProperties(@NotNull List<IElementAttribute> properties)
    {
        this.properties = properties;
        propIdToIndexMap = new HashMap<String, Integer>();
        for (int index = 0; index < properties.size(); index++)
        {
            IElementAttribute prop = properties.get(index);
            propIdToIndexMap.put(prop.getId(), index);
        }
    }

    @Override
    public int getPropertyIndex(String id)
    {
        Integer index = propIdToIndexMap.get(id);
        if (index == null)
        {
            return -1;
        }
        //throw new RuntimeException("There isn't any property with id: " + id);

        return index.intValue();
    }

    @Nullable
    @Override
    public abstract Object getValue(Object element, int index);

    @Override
    public abstract void setValue(Object element, int index, Object value);
}