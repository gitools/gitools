/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.panels.decorator;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.CategoricalElementDecorator;
import org.gitools.ui.dialog.EditCategoricalScaleDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class CategoricalElementDecoratorPanel extends AbstractElementDecoratorPanel
{

    private static final long serialVersionUID = 8422331422677024364L;

    private CategoricalElementDecorator decorator;

    private JComboBox valueCb;

    private ColorChooserLabel emptyCc;

    @NotNull
    private final String categoriesLabelText = " Categories:";
    @NotNull
    private final JLabel categoriesLabel = new JLabel(categoriesLabelText);

    public CategoricalElementDecoratorPanel(@NotNull Heatmap model)
    {
        super(model);

        this.decorator = (CategoricalElementDecorator) model.getActiveCellDecorator();

        final IElementAdapter adapter = decorator.getAdapter();

        valueProperties = new ArrayList<IndexedProperty>();
        loadAllProperties(valueProperties, adapter);

        createComponents();
    }

    private void createComponents()
    {
        valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));

        valueCb.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(@NotNull ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    valueChanged();
                }
            }
        });


        emptyCc = new ColorChooserLabel(decorator.getEmptyColor());
        emptyCc.setToolTipText("Empty cell color");
        emptyCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setEmptyColor(color);
            }
        });

        refresh();

        Dimension boxSpace = new Dimension(3, 3);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Value"));
        add(valueCb);

        int catCount = decorator.getCategoriesCount();
        categoriesLabel.setText(catCount + categoriesLabelText);
        Box box02 = new Box(BoxLayout.LINE_AXIS);
        box02.add(categoriesLabel);
        JButton categoryEditBtn = new JButton("edit");
        categoryEditBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                editCategoricalColorScale();
            }
        });
        box02.add(categoryEditBtn);
        add(box02);


        Box box01 = new Box(BoxLayout.LINE_AXIS);
        box01.add(new JLabel(" Empty"));
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(emptyCc);
        add(box01);
    }

    private void editCategoricalColorScale()
    {


        CategoricalElementDecorator elementDecorator = (CategoricalElementDecorator) model.getActiveCellDecorator();
        CategoricalColorScale scale = (CategoricalColorScale) elementDecorator.getScale();
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

            updateCategoriesLabel();
            elementDecorator.setCategories(newScalePoints);
        }
    }

    private void updateCategoriesLabel()
    {
        int catCount = decorator.getCategoriesCount();
        categoriesLabel.setText(catCount + categoriesLabelText);
    }

    private void refresh()
    {
        for (int i = 0; i < valueProperties.size(); i++)
            if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
            {
                valueCb.setSelectedIndex(i);
            }

        getTable().setSelectedLayer(decorator.getValueIndex());
    }

    private void valueChanged()
    {
        IndexedProperty propAdapter = (IndexedProperty) valueCb.getSelectedItem();

        model.switchActiveCellDecorator(propAdapter.getIndex());
        changeDecorator();

        decorator.setValueIndex(propAdapter.getIndex());
        updateCategoriesLabel();
        getTable().setSelectedLayer(propAdapter.getIndex());
    }

    private void changeDecorator()
    {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof CategoricalElementDecorator)
        {
            this.decorator = (CategoricalElementDecorator) elementDecorator;
        }
        else
        {
            return;
        }

        emptyCc.setColor(decorator.getEmptyColor());

    }

}
