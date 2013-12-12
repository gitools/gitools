package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.api.matrix.SortDirection;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.aggregation.AggregatorFactory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class SortSection implements ISettingsSection {

    public static List<SortDirection> DIRECTIONS = Arrays.asList(SortDirection.ASCENDING, SortDirection.DESCENDING);

    private JPanel mainPanel;
    private JComboBox defaultSortDirectionComboBox;
    private JComboBox defaultAggregatorComboBox;

    public SortSection(HeatmapLayer layer) {
        super();

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

    }

    @Override
    public String getName() {
        return "Sort";
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
}
