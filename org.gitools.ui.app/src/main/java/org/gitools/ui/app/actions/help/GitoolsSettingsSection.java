package org.gitools.ui.app.actions.help;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ConverterFactory;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;
import java.text.DecimalFormat;

import static com.jgoodies.binding.adapter.Bindings.bind;

public class GitoolsSettingsSection implements ISettingsSection {
    private JPanel panel;
    private JCheckBox IGVBox;
    private JTextField port;
    private JCheckBox tips;
    private JCheckBox portBox;
    private JTextField IGVUrl;

    public GitoolsSettingsSection(Settings settings) {
        PresentationModel<Settings> settingsModel = new PresentationModel<>(settings);


        bind(tips, settingsModel.getModel(Settings.PROPERTY_TIPS));
        bind(portBox, settingsModel.getModel(Settings.PROPERTY_PORT_ENABLED));
        bind(port, ConverterFactory.createStringConverter(settingsModel.getModel(Settings.PROPERTY_PORT),
                new DecimalFormat("#")));
        bind(IGVBox, settingsModel.getModel(Settings.PROPERTY_IGV_ENABLED));
        bind(IGVUrl, settingsModel.getModel(Settings.PROPERTY_IGV_URL));
    }

    @Override
    public String getName() {
        return "Usage Statistics";
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
