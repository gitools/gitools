/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.heatmap.panel.settings.headers;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterNamer;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.formatter.IntegerFormat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.jgoodies.binding.adapter.Bindings.bind;
import static com.jgoodies.binding.value.ConverterFactory.createStringConverter;

public class HierarchicalLevelsSection implements ISettingsSection {
    private JPanel root;

    private JTextField marginField;
    private JTextField visibleLevelsField;
    private JTextField maxLevels;
    private JComboBox paletteComboBox;


    public HierarchicalLevelsSection(final HierarchicalClusterHeatmapHeader heatmapHeader) {

        PresentationModel<HierarchicalClusterHeatmapHeader> header = new PresentationModel<>(heatmapHeader);

        bind(visibleLevelsField, header.getModel(HierarchicalClusterHeatmapHeader.PROPERTY_VISIBLE_LEVELS));
        bind(maxLevels, createStringConverter(header.getModel(HierarchicalClusterHeatmapHeader.PROPERTY_MAX_LEVELS), IntegerFormat.get()));

        List<String> paletteNames = new ArrayList<>(HierarchicalClusterNamer.palettes.keySet());
        paletteComboBox.setModel(
                new ComboBoxAdapter<>(
                    new SelectionInList<>(
                            paletteNames,
                            new PropertyAdapter<>(heatmapHeader, HierarchicalClusterHeatmapHeader.PROPERTY_COLOR_PALETTE)
                    )
                )
        );

    }

    @Override
    public String getName() {
        return "Levels";
    }

    @Override
    public JPanel getPanel() {
        return root;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    private void createUIComponents() {
        visibleLevelsField = new JTextField();
        maxLevels = new JTextField();
        paletteComboBox = new JComboBox();
    }
}
