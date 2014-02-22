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
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.heatmap.decorator.impl.BinaryDecorator;
import org.gitools.ui.app.utils.landf.MyWebColorChooserField;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;

public class BinaryDecoratorPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JTextField trueColor;
    private JComboBox comparator;
    private JFormattedTextField value;
    private JTextField falseColor;
    private JTextField emptyColor;
    private JCheckBox showValueCheckBox;

    public BinaryDecoratorPanel() {
        super("Binary scale", new BinaryDecorator());
    }


    @Override
    public void bind() {

        JFormattedTextField.AbstractFormatterFactory formatter = DecoratorPanelFormatters.getTenDecimalsFormatter();

        Bindings.bind(comparator, new SelectionInList<>(CutoffCmp.comparators, model(BinaryDecorator.PROPERTY_COMPARATOR)));
        Bindings.bind(value, model(BinaryDecorator.PROPERTY_CUTOFF));
        value.setFormatterFactory(formatter);
        Bindings.bind(trueColor, "color", model(BinaryDecorator.PROPERTY_COLOR));
        Bindings.bind(falseColor, "color", model(BinaryDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));
        Bindings.bind(emptyColor, "color", model(BinaryDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(showValueCheckBox, model(BinaryDecorator.PROPERTY_SHOW_VALUE));
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        trueColor = new MyWebColorChooserField();
        falseColor = new MyWebColorChooserField();
        emptyColor = new MyWebColorChooserField();
    }
}
