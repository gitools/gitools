package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.utils.formatter.DetailsBoxFormatter;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.gitools.utils.formatter.ITextFormatter;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class FormatSection implements ISettingsSection {

    private static List<? extends ITextFormatter> FORMATTERS = Arrays.asList(HeatmapTextFormatter.INSTANCE, DetailsBoxFormatter.INSTANCE);

    private JPanel mainPanel;
    private JComboBox cellFormatComboBox;
    private JComboBox detailsFormatComboBox;

    public FormatSection(final HeatmapLayer layer) {
        super();

        cellFormatComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                FORMATTERS,
                                new PropertyAdapter<>(layer, "shortFormatter")
                        )
                )
        );

        detailsFormatComboBox.setModel(
                new ComboBoxAdapter<>(
                    new SelectionInList<>(
                            FORMATTERS,
                            new PropertyAdapter<>(layer, "longFormatter")
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
