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
package org.gitools.ui.app.actions.edit;

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.heatmap.panel.settings.GridDetailsSection;
import org.gitools.ui.app.heatmap.panel.settings.HeatmapDetailsSection;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;

public class HeatmapSettingsAction extends HeatmapAction {

    public HeatmapSettingsAction() {
        super("Settings...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ISettingsSection detailsSection = new HeatmapDetailsSection(getHeatmap());
        ISettingsSection gridSection = new GridDetailsSection(getHeatmap());

        SettingsPanel settingsPanel = new SettingsPanel(
                "Heatmap settings",
                "General heatmap settings that apply to all the layers.",
                IconNames.logoNoText,
                detailsSection,
                gridSection
        );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, detailsSection.getName()) {
            @Override
            protected void apply() {
                getHeatmap().getLayers().updateLayers();
            }
        };

        dialog.setVisible(true);
    }
}