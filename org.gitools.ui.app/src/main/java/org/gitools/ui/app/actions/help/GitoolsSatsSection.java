package org.gitools.ui.app.actions.help;

import com.jgoodies.binding.PresentationModel;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;
import java.awt.*;

import static com.jgoodies.binding.adapter.Bindings.bind;

public class GitoolsSatsSection implements ISettingsSection {
    private JPanel panel;
    private JCheckBox allowUsageStatistics;
    private JLabel explanationLabel;

    public GitoolsSatsSection(Settings settings) {
        PresentationModel<Settings> settingsModel = new PresentationModel<>(settings);

        bind(allowUsageStatistics, settingsModel.getModel(Settings.PROPERTY_USAGE_STATS));
        explanationLabel.setPreferredSize(new Dimension(400, 200));
    }

    @Override
    public String getName() {
        return "Gitools Settings";
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
