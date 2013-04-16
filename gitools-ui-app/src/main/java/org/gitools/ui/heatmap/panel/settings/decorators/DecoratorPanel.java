package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.model.decorator.Decorator;

import javax.swing.*;
import java.util.List;

public abstract class DecoratorPanel
{
    private String name;
    private Class<? extends Decorator> decoratorClass;

    private PresentationModel<Decorator> panelModel;
    private Heatmap heatmap;

    public DecoratorPanel(String name, Decorator defaultDecorator)
    {
        this.name = name;
        this.decoratorClass = defaultDecorator.getClass();
        this.panelModel = new PresentationModel<Decorator>(defaultDecorator);
    }

    public boolean isValid(Object decorator)
    {
        if (decorator == null)
        {
            return false;
        }

        if (decoratorClass.equals(decorator.getClass()))
        {
            return true;
        }

        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setValue(Object decorator)
    {
         if (isValid(decorator))
         {
             getPanelModel().setBean((Decorator) decorator);
         }
    }

    protected PresentationModel<Decorator> getPanelModel()
    {
        return panelModel;
    }

    protected AbstractValueModel model(String propertyName)
    {
        AbstractValueModel model = getPanelModel().getModel(propertyName);
        return model;
    }

    protected List<HeatmapLayer> getLayers()
    {
        return heatmap.getLayers().toList();
    }

    public abstract void bind();

    public abstract JPanel getRootPanel();

    public String toString()
    {
        return getName();
    }

    public void setHeatmap(Heatmap heatmap)
    {
        this.heatmap = heatmap;
    }

    public Heatmap getHeatmap()
    {
        return heatmap;
    }
}
