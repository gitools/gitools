package org.gitools.ui.app.heatmap.panel.settings.layer.decorators;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DecoratorPanelFormatters {

    static JFormattedTextField.AbstractFormatterFactory getTenDecimalsFormatter() {
        return new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                NumberFormat format = DecimalFormat.getInstance();
                format.setMaximumFractionDigits(10);
                format.setRoundingMode(RoundingMode.HALF_UP);
                InternationalFormatter formatter = new InternationalFormatter(format);
                return formatter;
            }
        };
    }
}
