/*
 * #%L
 * gitools-utils
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
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.utils.colorscale.ColorScaleDrawer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ColorScalePanel extends JPanel implements PropertyChangeListener {

    private Heatmap heatmap;

    private ColorScaleDrawer drawer;

    public ColorScalePanel(Heatmap heatmap) {
        this.heatmap = heatmap;

        // Listen
        heatmap.getLayers().addPropertyChangeListener(HeatmapLayers.PROPERTY_TOP_LAYER, this);
        heatmap.getLayers().addPropertyChangeListener(Heatmap.PROPERTY_LAYERS, this);
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(HeatmapLayer.PROPERTY_DECORATOR, this);

        init();
    }

    private void init() {

        HeatmapLayer layer = heatmap.getLayers().getTopLayer();

        this.drawer = new ColorScaleDrawer(
                layer.getDecorator().getScale(),
                layer.getShortFormatter()
        );

        setPreferredSize(this.drawer.getSize());
        repaint();
    }

    public void update() {
        setPreferredSize(drawer.getSize());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = getSize();
        Rectangle box = new Rectangle(0, 0, size.width, size.height);
        Rectangle clip = g.getClipBounds();
        drawer.draw((Graphics2D) g, box, clip);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        init();
    }
}
