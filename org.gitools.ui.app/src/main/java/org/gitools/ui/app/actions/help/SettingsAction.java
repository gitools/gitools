package org.gitools.ui.app.actions.help;

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsAction extends BaseAction {
    public SettingsAction() {
        super("Settings");
        setDefaultEnabled(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute(Settings.getDefault());
    }

    public static void execute(Settings settings) {

        List<ISettingsSection> sections = new ArrayList<>();

        sections.add(new GitoolsSettingsSection(settings));
        sections.add(new GitoolsSatsSection(settings));


        SettingsPanel settingsPanel = new SettingsPanel(
                "Gitools settings",
                "",
                IconNames.logoNoText,
                sections
        );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, sections.get(0).getName()) {

            @Override
            protected void apply() {
                Settings.getDefault().save();
            }
        };

        dialog.open();
    }
}
