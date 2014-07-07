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
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.aggregation.AggregatorFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

public class DataManipulationSection implements ISettingsSection {

    public static List<SortDirection> DIRECTIONS = Arrays.asList(SortDirection.ASCENDING, SortDirection.DESCENDING);

    private JPanel mainPanel;
    private JComboBox defaultSortDirectionComboBox;
    private JComboBox defaultAggregatorComboBox;
    private JComboBox eventsFunctionComboBox;

    public DataManipulationSection(final HeatmapLayer layer) {
        super();

        layer.addPropertyChangeListener(HeatmapLayer.PROPERTY_DECORATOR, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setEventFunctions(layer);
            }
        });

        defaultSortDirectionComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                DIRECTIONS,
                                new PropertyAdapter<>(layer, "sortDirection")
                        )
                ));

        defaultAggregatorComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                AggregatorFactory.getAggregatorsArray(),
                                new PropertyAdapter<>(layer, "aggregator")
                        )
                )
        );

        setEventFunctions(layer);

    }

    private void setEventFunctions(HeatmapLayer layer) {
        eventsFunctionComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<NonEventToNullFunction>(
                                layer.getDecorator().getEventFunctionAlternatives(),
                                new PropertyAdapter<>(layer, "eventFunction")
                        )
                )
        );
    }

    @Override
    public String getName() {
        return "Data sort & manipulation";
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
}
