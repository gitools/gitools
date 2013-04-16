package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.model.decorator.impl.PValueDecorator;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class PValueDecoratorPanel extends DecoratorPanel
{
    private JPanel rootPanel;
    private JTextField minColor;
    private JTextField maxColor;
    private JTextField nonColor;
    private JTextField emptyColor;
    private JComboBox correctedValue;
    private JCheckBox useCorrection;
    private JSpinner significance;

    public PValueDecoratorPanel()
    {
        super("P-Value scale", new PValueDecorator());
    }

    @Override
    public void bind()
    {
        Bindings.bind(minColor, "color", model(PValueDecorator.PROPERTY_MIN_COLOR));
        Bindings.bind(maxColor, "color", model(PValueDecorator.PROPERTY_MAX_COLOR));
        Bindings.bind(emptyColor, "color", model(PValueDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(nonColor, "color", model(PValueDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));

        Bindings.bind(useCorrection, model(PValueDecorator.PROPERTY_USE_CORRECTION));

        Bindings.bind(correctedValue, new SelectionInList<HeatmapLayer>(
                getLayers(),
                new ValueHolder(),
                model(PValueDecorator.PROPERTY_CORRECTED_VALUE)
        ));

        significance.setModel(
                SpinnerAdapterFactory.createNumberAdapter(
                        model(PValueDecorator.PROPERTY_SIGNIFICANCE),
                        Double.valueOf(0.05),
                        Double.valueOf(0),
                        Double.valueOf(1),
                        Double.valueOf(0.01)
                )
        );
    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void createUIComponents()
    {
        minColor = new MyWebColorChooserField();
        maxColor = new MyWebColorChooserField();
        emptyColor = new MyWebColorChooserField();
        nonColor = new MyWebColorChooserField();
    }
}
