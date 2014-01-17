/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.actions.edit;

import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.heatmap.panel.settings.FormatSection;
import org.gitools.ui.heatmap.panel.settings.SortSection;
import org.gitools.ui.heatmap.panel.settings.layer.ColorScaleSection;
import org.gitools.ui.heatmap.panel.settings.layer.DetailsSection;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapLayerAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;

public class EditLayerAction extends HeatmapAction implements IHeatmapLayerAction {

    private HeatmapLayer layer;

    public EditLayerAction(String name) {
        super(name);
    }

    public EditLayerAction(HeatmapLayer layer) {
        super(layer.getName());

        this.layer = layer;
    }

    public HeatmapLayer getLayer() {
        return layer;
    }

    public void setLayer(HeatmapLayer layer) {
        this.layer = layer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getHeatmap().getLayers().setTopLayer(layer);

        ISettingsSection colorScaleSection = new ColorScaleSection(layer);
        ISettingsSection detailsSection = new DetailsSection(layer);
        ISettingsSection formatSection = new FormatSection(layer);
        ISettingsSection sortSection = new SortSection(layer);

        SettingsPanel settingsPanel = new SettingsPanel(
                "Layer '" + layer.getName() + "' settings",
                layer.getDescription(),
                IconNames.logoNoText,
                colorScaleSection,
                formatSection,
                sortSection,
                detailsSection
                );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, colorScaleSection.getName());
        dialog.setVisible(true);
    }

    @Override
    public void onConfigure(HeatmapLayer object, HeatmapPosition position) {
        setLayer(object);
    }

}
