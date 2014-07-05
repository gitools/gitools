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
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import org.gitools.heatmap.decorator.impl.PValueLogSumDecorator;
import org.gitools.ui.core.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class PValueLogSumPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JTextField leftMinColor;
    private JTextField leftMaxColor;
    private JTextField nonSigColor;
    private JTextField rightMinColor;
    private JTextField rightMaxColor;
    private JTextField emptyColor;
    private JSpinner significance;
    private JCheckBox showValueCheckBox;
    private JFormattedTextField translatedSig;
    private JFormattedTextField limit;

    public PValueLogSumPanel() {
        super("P-value log-sum scale", new PValueLogSumDecorator());
    }

    @Override
    public void bind() {

        Bindings.bind(leftMinColor, "color", model(PValueLogSumDecorator.PROPERTY_LEFT_MIN_COLOR));
        Bindings.bind(leftMaxColor, "color", model(PValueLogSumDecorator.PROPERTY_LEFT_MAX_COLOR));
        Bindings.bind(rightMinColor, "color", model(PValueLogSumDecorator.PROPERTY_RIGHT_MIN_COLOR));
        Bindings.bind(rightMaxColor, "color", model(PValueLogSumDecorator.PROPERTY_RIGHT_MAX_COLOR));
        Bindings.bind(nonSigColor, "color", model(PValueLogSumDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));
        Bindings.bind(emptyColor, "color", model(PValueLogSumDecorator.PROPERTY_EMPTY_COLOR));

        JFormattedTextField.AbstractFormatterFactory formatter = DecoratorPanelFormatters.getTenDecimalsFormatter();
        Bindings.bind(limit, model(PValueLogSumDecorator.PROPERTY_LIMIT));
        limit.setFormatterFactory(formatter);


        significance.setModel(
                SpinnerAdapterFactory.createNumberAdapter(
                        model(PValueLogSumDecorator.PROPERTY_SIGNIFICANCE),
                        Double.valueOf(0.05),
                        Double.valueOf(0),
                        Double.valueOf(1),
                        Double.valueOf(0.01)
                )
        );
        Bindings.bind(translatedSig, model(PValueLogSumDecorator.PROPERTY_SIGNIFICANCE_TRANSLATION));

        Bindings.bind(showValueCheckBox, model(PValueLogSumDecorator.PROPERTY_SHOW_VALUE));
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        this.leftMinColor = new MyWebColorChooserField();
        this.leftMaxColor = new MyWebColorChooserField();
        this.rightMinColor = new MyWebColorChooserField();
        this.rightMaxColor = new MyWebColorChooserField();
        this.emptyColor = new MyWebColorChooserField();
        this.nonSigColor = new MyWebColorChooserField();
    }
}
