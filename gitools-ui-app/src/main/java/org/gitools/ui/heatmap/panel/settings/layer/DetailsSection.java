package org.gitools.ui.heatmap.panel.settings.layer;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;

public class DetailsSection implements ISettingsSection {

    private JPanel rootPanel;
    private JTextArea layerDescription;
    private JTextField layerDescriptionLink;
    private JTextField layerValueLink;

    public DetailsSection(HeatmapLayer heatmapLayer) {
        PresentationModel<HeatmapLayer> layer = new PresentationModel<>(heatmapLayer);

        // Bind value controls
        Bindings.bind(layerDescription, layer.getModel(HeatmapLayer.PROPERTY_DESCRIPTION));
        Bindings.bind(layerDescriptionLink, layer.getModel(HeatmapLayer.PROPERTY_DESCRIPTION_URL));
        Bindings.bind(layerValueLink, layer.getModel(HeatmapLayer.PROPERTY_VALUE_URL));


    }

    @Override
    public String getName() {
        return "Details";
    }

    @Override
    public JPanel getPanel() {
        return rootPanel;
    }
}
