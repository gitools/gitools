package org.gitools.matrix.model;

import com.jgoodies.binding.beans.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleMatrixLayer extends Model implements IMatrixLayer
{
    private String id;
    private String name;
    private String description;

    @XmlElement(name = "value-type")
    private Class<?> valueClass;

    public SimpleMatrixLayer()
    {
        // JAXB requirement
    }

    public SimpleMatrixLayer(String id, Class<?> valueClass)
    {
        this(id, valueClass, null, null);
    }

    public SimpleMatrixLayer(String id, Class<?> valueClass, String name, String description)
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
