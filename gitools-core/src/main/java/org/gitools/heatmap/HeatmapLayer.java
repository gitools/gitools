package org.gitools.heatmap;

import org.gitools.matrix.model.SimpleLayerDescriptor;
import org.gitools.matrix.model.element.ILayerDescriptor;
import org.gitools.model.decorator.ElementDecorator;


public class HeatmapLayer extends SimpleLayerDescriptor implements ILayerDescriptor
{
    private ElementDecorator decorator;

    public HeatmapLayer()
    {
        super();

        // JAXB requirement
    }

    public HeatmapLayer(String id, Class<?> valueClass, ElementDecorator decorator)
    {
        super(id, valueClass);

        this.decorator = decorator;
    }

    public ElementDecorator getDecorator()
    {
        return decorator;
    }

    public void setDecorator(ElementDecorator decorator)
    {
        this.decorator = decorator;
    }




}
