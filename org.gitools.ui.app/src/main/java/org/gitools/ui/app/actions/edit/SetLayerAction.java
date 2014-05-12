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
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapLayerAction;

import java.awt.event.ActionEvent;

public class SetLayerAction extends HeatmapAction implements IHeatmapLayerAction {

    private HeatmapLayer layer;

    public SetLayerAction(String name) {
        super(name);
    }

    public SetLayerAction(HeatmapLayer layer) {
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

    }

    @Override
    public void onConfigure(HeatmapLayer object, HeatmapPosition position) {
        setLayer(object);
    }

}
