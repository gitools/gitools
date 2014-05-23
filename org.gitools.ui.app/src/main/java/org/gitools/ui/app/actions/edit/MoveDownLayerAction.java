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
package org.gitools.ui.app.actions.edit;

import org.gitools.heatmap.HeatmapLayer;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapLayerAction;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;
import java.util.List;

public class MoveDownLayerAction extends HeatmapAction implements IHeatmapLayerAction {

    private HeatmapLayer layer;

    public MoveDownLayerAction() {
        super("Move down");

        setSmallIconFromResource(IconNames.moveDown16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Move the header one position up
        HeatmapLayer topLayer = getHeatmap().getLayers().getTopLayer();
        List<HeatmapLayer> layers = getHeatmap().getLayers().asList();
        int index = getHeaderCurrentIndex();
        layers.set(index, layers.get(index + 1));
        layers.set(index + 1, layer);
        getHeatmap().getLayers().setTopLayer(topLayer);

        // Fire headers events
        getHeatmap().getLayers().updateLayers();

        Application.get().setStatusText("Move down layer '" + layer.getName() + "'");
    }

    private int getHeaderCurrentIndex() {
        return getHeatmap().getLayers().indexOf(layer.getId());
    }

    @Override
    public void onConfigure(HeatmapLayer object, HeatmapPosition position) {
        this.layer = object;
        setEnabled(layer != null && (getHeaderCurrentIndex() + 1) < getHeatmap().getLayers().size());
    }
}
