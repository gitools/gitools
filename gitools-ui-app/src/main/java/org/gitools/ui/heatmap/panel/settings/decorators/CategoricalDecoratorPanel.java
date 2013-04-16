package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.model.decorator.impl.CategoricalDecorator;
import org.gitools.ui.dialog.EditCategoricalScaleDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.utils.landf.MyWebColorChooserField;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoricalDecoratorPanel extends DecoratorPanel
{
    private JPanel rootPanel;
    private JButton editButton;
    private JLabel totalCategories;
    private JTextField emptyColor;

    public CategoricalDecoratorPanel()
    {
        super("Categorical scale", new CategoricalDecorator());

        editButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                editCategoricalColorScale();
            }
        });
    }

    private void editCategoricalColorScale()
    {


        CategoricalDecorator elementDecorator = (CategoricalDecorator) getPanelModel().getBean();
        CategoricalColorScale scale = elementDecorator.getScale();
        ColorScalePoint[] scalePoints = scale.getPointObjects();

        ColoredLabel[] coloredLabels = new ColoredLabel[scalePoints.length];
        int index = 0;
        for (ColorScalePoint sp : scalePoints)
        {
            coloredLabels[index] = new ColoredLabel(sp.getValue(), sp.getName(), sp.getColor());
            index++;
        }

        EditCategoricalScaleDialog dialog = new EditCategoricalScaleDialog(AppFrame.get(), coloredLabels);
        dialog.getPage().setValueMustBeNumeric(true);
        dialog.setVisible(true);
        if (dialog.getReturnStatus() == AbstractDialog.RET_OK)
        {
            coloredLabels = dialog.getPage().getColoredLabels();

            ColorScalePoint[] newScalePoints = new ColorScalePoint[coloredLabels.length];
            index = 0;
            for (ColoredLabel cl : coloredLabels)
            {
                newScalePoints[index] = new ColorScalePoint(Double.parseDouble(cl.getValue()), cl.getColor(), cl.getDisplayedLabel());
                index++;
            }

            totalCategories.setText(String.valueOf(newScalePoints.length));
            elementDecorator.setCategories(newScalePoints);
        }
    }


    @Override
    public void bind()
    {
        Bindings.bind(totalCategories, "text", model(CategoricalDecorator.PROPERTY_CATEGORIES_COUNT));
        Bindings.bind(emptyColor, "color", model(CategoricalDecorator.PROPERTY_EMPTY_COLOR));
    }

    @Override
    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    private void createUIComponents()
    {
        this.emptyColor = new MyWebColorChooserField();
    }
}
