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
package org.gitools.ui.app.heatmap.panel.settings;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.formatter.ScientificHeatmapTextFormatter;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class FormatSection implements ISettingsSection {

    private static List<? extends ITextFormatter> FORMATTERS = Arrays.asList(
            HeatmapTextFormatter.TWO_DECIMALS,
            ScientificHeatmapTextFormatter.INSTANCE,
            HeatmapTextFormatter.NO_DECIMALS,
            HeatmapTextFormatter.FOUR_DECIMALS,
            HeatmapTextFormatter.ONE_DECIMALS
    );

    private JPanel mainPanel;
    private JComboBox cellFormatComboBox;
    private JComboBox detailsFormatComboBox;

    public FormatSection(final HeatmapLayer layer) {
        super();

        cellFormatComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                FORMATTERS,
                                new PropertyAdapter<>(layer, HeatmapLayer.PROPERTY_SHORT_FORMATTER)
                        )
                )
        );

        detailsFormatComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                FORMATTERS,
                                new PropertyAdapter<>(layer, HeatmapLayer.PROPERTY_LONG_FORMATTER)
                        )
                ));

    }


    @Override
    public String getName() {
        return "Format";
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

}
