package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrixLayer;
import org.gitools.matrix.model.SimpleMatrixLayer;
import org.gitools.model.decorator.Decorator;
import org.gitools.utils.EventUtils;


public class HeatmapLayer extends SimpleMatrixLayer implements IMatrixLayer
{
    public static final String PROPERTY_DECORATOR = "decorator";

    private Decorator decorator;

    public HeatmapLayer()
    {
        super();

        // JAXB requirement
    }

    public HeatmapLayer(String id, Class<?> valueClass, Decorator decorator)
    {
        super(id, valueClass);

        this.decorator = decorator;
    }

    public Decorator getDecorator()
    {
        return decorator;
    }

    public void setDecorator(Decorator decorator)
    {
        Decorator oldValue = this.decorator;
        this.decorator = decorator;

        firePropertyChange(PROPERTY_DECORATOR, oldValue, decorator);
        EventUtils.moveListeners(oldValue, decorator);
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
