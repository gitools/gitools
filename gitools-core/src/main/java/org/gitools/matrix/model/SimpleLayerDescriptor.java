package org.gitools.matrix.model;

import org.gitools.matrix.model.element.ILayerDescriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleLayerDescriptor implements ILayerDescriptor
{

    private String id;
    private String name;
    private String description;

    @XmlElement(name = "value-type")
    private Class<?> valueClass;

    public SimpleLayerDescriptor()
    {
        // JAXB requirement
    }

    public SimpleLayerDescriptor(String id, Class<?> valueClass)
    {
        this(id, valueClass, null, null);
    }

    public SimpleLayerDescriptor(String id, Class<?> valueClass, String name, String description)
    {
        this.id = id;
        this.valueClass = valueClass;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        if (name == null)
        {
            return id;
        }

        return name;
    }

    @Override
    public String getDescription()
    {
        if (description == null)
        {
            return "";
        }

        return description;
    }

    @Override
    public Class<?> getValueClass()
    {
        return valueClass;
    }
}
