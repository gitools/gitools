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
package org.gitools.ui.app.heatmap.panel;

import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.heatmap.drawer.HeatmapLayerBodyDrawer;
import org.gitools.ui.app.heatmap.drawer.SelectionLayerBodyDrawer;

import javax.swing.*;
import java.awt.*;

public class HeatmapBodyPanel extends JLayeredPane {

    private HeatmapLayerBodyDrawer drawer;
    private AbstractHeatmapPanel heatmapLayerPanel;
    private SelectionLayerBodyDrawer selectionDrawer;
    private AbstractHeatmapPanel selectionLayerPanel;


    public HeatmapBodyPanel(Heatmap heatmap) {
        super();

        drawer = new HeatmapLayerBodyDrawer(heatmap);
        heatmapLayerPanel = new AbstractHeatmapPanel(heatmap, drawer);

        selectionDrawer = new SelectionLayerBodyDrawer(heatmap);
        selectionLayerPanel = new AbstractHeatmapPanel(heatmap, selectionDrawer);
        selectionLayerPanel.setOpaque(false);

        setOpaque(false);

        setBorder(null);
        setBackground(Color.WHITE);

        Dimension size = drawer.getSize();
        setPreferredSize(size);

        heatmapLayerPanel.setBounds(0, 0, size.width, size.height);
        add(heatmapLayerPanel, new Integer(10));
        selectionLayerPanel.setBounds(0, 0, size.width, size.height);
        add(selectionLayerPanel, new Integer(20));

 /*     JLabel label = new JLabel("HOLA");
        label.setFont(label.getFont().deriveFont(44f));
        label.setForeground(new Color(0,0,0,125));
        label.setBounds(10, 10, 140, 140);
        add(label, 100);*/

    }

    public HeatmapLayerBodyDrawer getDrawer() {
        return drawer;
    }


    public void updateSize() {
        heatmapLayerPanel.updateSize();
        selectionLayerPanel.updateSize();
    }
}
