package org.gitools.ui.heatmap.panel.settings.headers;

import com.jgoodies.binding.PresentationModel;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import static com.jgoodies.binding.adapter.Bindings.bind;

import javax.swing.*;

public class ColorsSection implements ISettingsSection {
    private JPanel root;
    private JTextField textColorTextField;
    private JTextField backgroundColorTextField;

    public ColorsSection(HeatmapHeader heatmapHeader) {

        PresentationModel<HeatmapHeader> header = new PresentationModel<>(heatmapHeader);

        bind(textColorTextField, "color", header.getModel(HeatmapHeader.PROPERTY_LABEL_COLOR));
        bind(backgroundColorTextField, "color", header.getModel(HeatmapHeader.PROPERTY_BACKGROUND_COLOR));

    }

    @Override
    public String getName() {
        return "Colors";
    }

    @Override
    public JPanel getPanel() {
        return root;
    }

    private void createUIComponents() {
        textColorTextField = new MyWebColorChooserField();
        backgroundColorTextField = new MyWebColorChooserField();
    }
}
