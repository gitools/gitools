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
        return "Usage Stats";
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
