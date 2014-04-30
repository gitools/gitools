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
package org.gitools.ui.app.heatmap.panel.settings.layer.decorators;

import com.jgoodies.binding.adapter.Bindings;
import org.gitools.heatmap.decorator.impl.CategoricalDecorator;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.ui.app.actions.data.DetectCategoriesAction;
import org.gitools.ui.app.heatmap.header.wizard.coloredlabels.ColoredLabelsGroupsPage;
import org.gitools.ui.core.utils.landf.MyWebColorChooserField;
import org.gitools.ui.platform.progress.ProgressUtils;
import org.gitools.utils.color.ColorGenerator;
import org.gitools.utils.color.ColorRegistry;
import org.gitools.utils.colorscale.ColorScalePoint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CategoricalDecoratorPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JLabel totalCategories;
    private JTextField emptyColor;
    private JCheckBox showValueCheckBox;
    private JPanel pagePanel;
    private JButton detectCategoriesButton;
    private ColoredLabelsGroupsPage categoriesPage;

    public CategoricalDecoratorPanel() {
        super("Categorical scale", new CategoricalDecorator());

        categoriesPage = new ColoredLabelsGroupsPage(generateColoredLabels());
        pagePanel.setLayout(new BoxLayout(pagePanel, BoxLayout.PAGE_AXIS));
        pagePanel.add(categoriesPage);
        //categoriesPage.setMaximumSize(new Dimension(700, 300));
        detectCategoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detectCategories();
            }
        });
    }

    private void detectCategories() {
        DetectCategoriesAction detection = new DetectCategoriesAction();
        detection.setParentWindow(ProgressUtils.getParentGlassPaneWindow(this.getRootPanel()));
        detection.run();

        List<ColoredLabel> coloredLabels = new ArrayList<>();
        ColorGenerator cg = new ColorGenerator();

        for (double c : detection.getCategories()) {
            String value = Double.toString(c);
            coloredLabels.add(new ColoredLabel(value, cg.next(value)));
        }
        if (coloredLabels.size() > 0) {
            categoriesPage.setColoredLabels(coloredLabels);
        }
    }

    private CategoricalDecorator getDecorator() {
        return (CategoricalDecorator) getPanelModel().getBean();
    }


    private void updateCategories() {

        categoriesPage.setColoredLabels(generateColoredLabels());

    }


    @Override
    public void bind() {
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateDecorator();
                totalCategories.setText(String.valueOf(getDecorator().getCategories().length));
            }
        };
        categoriesPage.addPropertyChangeListener(listener);

        totalCategories.setText(String.valueOf(getDecorator().getCategories().length));
        Bindings.bind(emptyColor, "color", model(CategoricalDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(showValueCheckBox, model(CategoricalDecorator.PROPERTY_SHOW_VALUE));
        updateCategories();
    }

    private void updateDecorator() {
        List<ColoredLabel> coloredLabels = categoriesPage.getColoredLabels();

        ColorScalePoint[] newScalePoints = new ColorScalePoint[coloredLabels.size()];
        int index = 0;
        ColorRegistry cr = ColorRegistry.get();
        for (ColoredLabel cl : coloredLabels) {
            newScalePoints[index] = new ColorScalePoint(Double.parseDouble(cl.getValue()), cl.getColor(), cl.getDisplayedLabel());
            cr.registerId(cl.getValue(), cl.getColor());
            index++;
        }

        totalCategories.setText(String.valueOf(newScalePoints.length));
        getDecorator().setCategories(newScalePoints);

    }


    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        this.emptyColor = new MyWebColorChooserField();
    }

    public List<ColoredLabel> generateColoredLabels() {
        List<ColoredLabel> coloredLabels = new ArrayList<>(getDecorator().getCategories().length);
        for (ColorScalePoint sp : getDecorator().getCategories()) {
            coloredLabels.add(new ColoredLabel(sp.getValue(), sp.getName(), sp.getColor()));
        }
        return coloredLabels;
    }
}
