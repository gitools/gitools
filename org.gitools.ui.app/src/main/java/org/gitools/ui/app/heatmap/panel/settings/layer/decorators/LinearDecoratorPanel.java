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
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.impl.LinearDecorator;
import org.gitools.ui.app.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class LinearDecoratorPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JFormattedTextField minValue;
    private JTextField minColor;
    private JFormattedTextField midValue;
    private JTextField midColor;
    private JFormattedTextField maxValue;
    private JTextField maxColor;
    private JTextField emptyColor;
    private JCheckBox showValueCheckBox;

    public LinearDecoratorPanel(String name, Decorator defaultDecorator) {
        super(name, defaultDecorator);
    }

    @Override
    public void bind() {
        Bindings.bind(minValue, model(LinearDecorator.PROPERTY_MIN_VALUE));
        Bindings.bind(midValue, model(LinearDecorator.PROPERTY_MID_VALUE));
        Bindings.bind(maxValue, model(LinearDecorator.PROPERTY_MAX_VALUE));

        Bindings.bind(minColor, "color", model(LinearDecorator.PROPERTY_MIN_COLOR));
        Bindings.bind(midColor, "color", model(LinearDecorator.PROPERTY_MID_COLOR));
        Bindings.bind(maxColor, "color", model(LinearDecorator.PROPERTY_MAX_COLOR));
        Bindings.bind(emptyColor, "color", model(LinearDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(showValueCheckBox, model(LinearDecorator.PROPERTY_SHOW_VALUE));
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        this.minColor = new MyWebColorChooserField();
        this.midColor = new MyWebColorChooserField();
        this.maxColor = new MyWebColorChooserField();
        this.emptyColor = new MyWebColorChooserField();
    }
}
