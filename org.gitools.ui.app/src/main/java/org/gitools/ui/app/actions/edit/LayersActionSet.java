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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.actions.HeatmapDynamicActionSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class LayersActionSet extends HeatmapDynamicActionSet {

    public LayersActionSet() {
        super("Layers", KeyEvent.VK_L);
    }

    @Override
    protected void populateMenu(Heatmap heatmap, JMenu menu) {
        menu.removeAll();

        HeatmapLayers layers = heatmap.getLayers();
        int i=1;
        for (HeatmapLayer layer : layers) {

            JMenuItem menuItem = new JMenuItem(new EditLayerAction(layer));
            if (layers.getTopLayer().equals(layer)) {
                menuItem.setFont(menuItem.getFont().deriveFont(Font.BOLD));
            }

            if (i < 10) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke('0' + i, InputEvent.CTRL_MASK));
            }

            menu.add(menuItem);
            i++;
        }

        menu.addSeparator();
        menu.add(Actions.addLayerHeader);
    }
}
