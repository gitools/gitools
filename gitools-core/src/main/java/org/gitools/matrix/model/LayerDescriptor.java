package org.gitools.matrix.model;

import org.gitools.matrix.model.element.ILayerDescriptor;

public class LayerDescriptor implements ILayerDescriptor
{

    private String id;
    private String name;
    private String description;
    private Class<?> valueClass;

    public LayerDescriptor(String id, Class<?> valueClass)
    {
        this(id, valueClass, id, id);
    }

    public LayerDescriptor(String id, Class<?> valueClass, String name, String description)
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
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public Class<?> getValueClass()
    {
        return valueClass;
    }
}
