package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.model.decorator.Decorator;
import org.gitools.utils.EventUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DecoratorPanelContainer extends JPanel implements PropertyChangeListener
{
    private Heatmap heatmap;

    private ValueModel currentPanelModel;
    private List<DecoratorPanel> panels;

    public DecoratorPanelContainer()
    {
        super();

        setLayout(new CardLayout());

        this.currentPanelModel = new ValueHolder();
        this.currentPanelModel.addValueChangeListener(this);
    }

    public void init(List<DecoratorPanel> panels, Heatmap heatmap)
    {
        this.heatmap = heatmap;
        this.panels = panels;
        this.heatmap.getLayers().addPropertyChangeListener(HeatmapLayers.PROPERTY_TOP_LAYER, this);

        removeAll();
        for (DecoratorPanel panel : panels) {

            add(panel.getRootPanel(), panel.getName());

            if (panel.isValid(getCurrentDecorator()))
            {
                getCurrentPanelModel().setValue( panel );
                panel.setValue(getCurrentDecorator());
            }

            panel.setHeatmap(heatmap);
            panel.bind();
        }

        updateFromHeatmap();

        showCurrentPanel();
    }

    private Decorator getCurrentDecorator()
    {
        return heatmap.getLayers().getTopLayer().getDecorator();
    }

    private void setCurrentDecorator(Decorator decorator)
    {
        heatmap.getLayers().getTopLayer().setDecorator(decorator);
    }

    private void showCurrentPanel()
    {
        getCardLayout().show(this, getCurrentPanel().getName());
    }

    private DecoratorPanel getCurrentPanel()
    {
        return (DecoratorPanel) getCurrentPanelModel().getValue();
    }

    public ValueModel getCurrentPanelModel()
    {
        return this.currentPanelModel;
    }

    private CardLayout getCardLayout()
    {
        return (CardLayout) DecoratorPanelContainer.this.getLayout();
    }

    private void updateFromHeatmap()
    {
        Decorator decorator = getCurrentDecorator();

        for (DecoratorPanel panel : panels)
        {
            if (panel.isValid(decorator))
            {
                getCurrentPanelModel().setValue( panel );
                panel.setValue(decorator);
            }
        }
    }

    private void updateFromPanel()
    {
        Decorator panelDefault = getCurrentPanel().getPanelModel().getBean();
        Decorator currentDecorator = getCurrentDecorator();

        if (!panelDefault.getClass().isAssignableFrom(currentDecorator.getClass()))
        {
            setCurrentDecorator(panelDefault);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (EventUtils.isAny(evt, HeatmapLayers.class, HeatmapLayers.PROPERTY_TOP_LAYER))
        {
            updateFromHeatmap();
        } else {
            updateFromPanel();
        }
        showCurrentPanel();
    }
}
