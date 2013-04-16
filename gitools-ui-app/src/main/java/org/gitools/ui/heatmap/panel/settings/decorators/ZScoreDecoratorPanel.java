package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.model.decorator.impl.ZScoreDecorator;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class ZScoreDecoratorPanel extends DecoratorPanel
{
    private JPanel rootPanel;
    private JTextField leftMinColor;
    private JTextField leftMaxColor;
    private JTextField nonSigColor;
    private JTextField rightMinColor;
    private JTextField rightMaxColor;
    private JTextField emptyColor;
    private JSpinner significance;
    private JCheckBox filterByCheckBox;
    private JComboBox filterValue;

    public ZScoreDecoratorPanel()
    {
        super("Z-Score scale", new ZScoreDecorator());
    }

    @Override
    public void bind()
    {

        Bindings.bind(leftMinColor, "color", model(ZScoreDecorator.PROPERTY_LEFT_MIN_COLOR));
        Bindings.bind(leftMaxColor, "color", model(ZScoreDecorator.PROPERTY_LEFT_MAX_COLOR));
        Bindings.bind(rightMinColor, "color", model(ZScoreDecorator.PROPERTY_RIGHT_MIN_COLOR));
        Bindings.bind(rightMaxColor, "color", model(ZScoreDecorator.PROPERTY_RIGHT_MAX_COLOR));
        Bindings.bind(nonSigColor, "color", model(ZScoreDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));
        Bindings.bind(emptyColor, "color", model(ZScoreDecorator.PROPERTY_EMPTY_COLOR));

        significance.setModel(
                SpinnerAdapterFactory.createNumberAdapter(
                        model(ZScoreDecorator.PROPERTY_SIGNIFICANCE),
                        Double.valueOf(0.05),
                        Double.valueOf(0),
                        Double.valueOf(1),
                        Double.valueOf(0.01)
                )
        );

        Bindings.bind(filterByCheckBox, model(ZScoreDecorator.PROPERTY_USE_CORRECTION));
        Bindings.bind(filterValue, new SelectionInList<HeatmapLayer>(
                getLayers(),
                new ValueHolder(),
                model(ZScoreDecorator.PROPERTY_CORRECTED_VALUE))
        );
    }

    @Override
    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void createUIComponents()
    {
        this.leftMinColor = new MyWebColorChooserField();
        this.leftMaxColor = new MyWebColorChooserField();
        this.rightMinColor = new MyWebColorChooserField();
        this.rightMaxColor = new MyWebColorChooserField();
        this.emptyColor = new MyWebColorChooserField();
        this.nonSigColor = new MyWebColorChooserField();
    }
}
