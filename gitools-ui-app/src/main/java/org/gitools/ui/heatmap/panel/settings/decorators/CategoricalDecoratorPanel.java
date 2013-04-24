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
package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.core.heatmap.header.ColoredLabel;
import org.gitools.core.model.decorator.impl.CategoricalDecorator;
import org.gitools.ui.dialog.EditCategoricalScaleDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.utils.landf.MyWebColorChooserField;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CategoricalDecoratorPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JButton editButton;
    private JLabel totalCategories;
    private JTextField emptyColor;
    private JCheckBox showValueCheckBox;

    public CategoricalDecoratorPanel() {
        super("Categorical scale", new CategoricalDecorator());

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCategoricalColorScale();
            }
        });

    }

    private CategoricalDecorator getDecorator() {
        return (CategoricalDecorator) getPanelModel().getBean();
    }


    private void editCategoricalColorScale() {


        CategoricalDecorator elementDecorator = getDecorator();
        CategoricalColorScale scale = elementDecorator.getScale();
        ColorScalePoint[] scalePoints = scale.getPointObjects();

        ColoredLabel[] coloredLabels = new ColoredLabel[scalePoints.length];
        int index = 0;
        for (ColorScalePoint sp : scalePoints) {
            coloredLabels[index] = new ColoredLabel(sp.getValue(), sp.getName(), sp.getColor());
            index++;
        }

        EditCategoricalScaleDialog dialog = new EditCategoricalScaleDialog(AppFrame.get(), coloredLabels);
        dialog.getPage().setValueMustBeNumeric(true);
        dialog.setVisible(true);
        if (dialog.getReturnStatus() == AbstractDialog.RET_OK) {
            coloredLabels = dialog.getPage().getColoredLabels();

            ColorScalePoint[] newScalePoints = new ColorScalePoint[coloredLabels.length];
            index = 0;
            for (ColoredLabel cl : coloredLabels) {
                newScalePoints[index] = new ColorScalePoint(Double.parseDouble(cl.getValue()), cl.getColor(), cl.getDisplayedLabel());
                index++;
            }

            totalCategories.setText(String.valueOf(newScalePoints.length));
            elementDecorator.setCategories(newScalePoints);
        }
    }


    @Override
    public void bind() {
        ValueModel categories = model(CategoricalDecorator.PROPERTY_CATEGORIES);
        categories.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                totalCategories.setText(String.valueOf(getDecorator().getCategories().length));
            }
        });
        totalCategories.setText(String.valueOf(getDecorator().getCategories().length));
        Bindings.bind(emptyColor, "color", model(CategoricalDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(showValueCheckBox, model(CategoricalDecorator.PROPERTY_SHOW_VALUE));
    }


    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        this.emptyColor = new MyWebColorChooserField();
    }
}
