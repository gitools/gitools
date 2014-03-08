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
