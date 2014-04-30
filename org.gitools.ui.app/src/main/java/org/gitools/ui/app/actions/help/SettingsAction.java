/*
 * #%L
 * org.gitools.ui.app
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
package org.gitools.ui.app.actions.help;

import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.core.actions.AbstractAction;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsAction extends AbstractAction {
    public SettingsAction() {
        super("Settings");
        setDefaultEnabled(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute(Settings.get());
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
                Settings.get().save();
            }
        };

        dialog.open();
    }
}
