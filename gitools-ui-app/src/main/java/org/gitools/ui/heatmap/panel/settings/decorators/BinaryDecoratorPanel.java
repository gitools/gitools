package org.gitools.ui.heatmap.panel.settings.decorators;


import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.model.decorator.impl.BinaryDecorator;
import org.gitools.ui.utils.landf.MyWebColorChooserField;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;

public class BinaryDecoratorPanel extends DecoratorPanel
{
    private JPanel rootPanel;
    private JTextField trueColor;
    private JComboBox comparator;
    private JFormattedTextField value;
    private JTextField falseColor;
    private JTextField emptyColor;

    public BinaryDecoratorPanel()
    {
        super("Binary scale", new BinaryDecorator());
    }


    @Override
    public void bind()
    {
        Bindings.bind(comparator, new SelectionInList<CutoffCmp>(CutoffCmp.comparators, model(BinaryDecorator.PROPERTY_COMPARATOR)));
        Bindings.bind(value, model(BinaryDecorator.PROPERTY_CUTOFF));
        Bindings.bind(trueColor, "color", model(BinaryDecorator.PROPERTY_COLOR));
        Bindings.bind(falseColor, "color", model(BinaryDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));
        Bindings.bind(emptyColor, "color", model(BinaryDecorator.PROPERTY_EMPTY_COLOR));
    }

    @Override
    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void createUIComponents()
    {
        trueColor = new MyWebColorChooserField();
        falseColor = new MyWebColorChooserField();
        emptyColor = new MyWebColorChooserField();
    }
}
