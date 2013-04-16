package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import org.gitools.model.decorator.Decorator;
import org.gitools.model.decorator.impl.LinearDecorator;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class LinearDecoratorPanel extends DecoratorPanel
{
    private JPanel rootPanel;
    private JFormattedTextField minValue;
    private JTextField minColor;
    private JFormattedTextField midValue;
    private JTextField midColor;
    private JFormattedTextField maxValue;
    private JTextField maxColor;
    private JTextField emptyColor;

    public LinearDecoratorPanel(String name, Decorator defaultDecorator)
    {
        super(name, defaultDecorator);
    }

    @Override
    public void bind()
    {
        Bindings.bind(minValue, model(LinearDecorator.PROPERTY_MIN_VALUE));
        Bindings.bind(midValue, model(LinearDecorator.PROPERTY_MID_VALUE));
        Bindings.bind(maxValue, model(LinearDecorator.PROPERTY_MAX_VALUE));

        Bindings.bind(minColor, "color", model(LinearDecorator.PROPERTY_MIN_COLOR));
        Bindings.bind(midColor, "color", model(LinearDecorator.PROPERTY_MID_COLOR));
        Bindings.bind(maxColor, "color", model(LinearDecorator.PROPERTY_MAX_COLOR));
        Bindings.bind(emptyColor, "color", model(LinearDecorator.PROPERTY_EMPTY_COLOR));
    }

    @Override
    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void createUIComponents()
    {
        this.minColor = new MyWebColorChooserField();
        this.midColor = new MyWebColorChooserField();
        this.maxColor = new MyWebColorChooserField();
        this.emptyColor = new MyWebColorChooserField();
    }
}
