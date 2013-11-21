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
package org.gitools.ui.heatmap.panel;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.drawer.ColorScaleDrawer;

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
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(HeatmapLayer.PROPERTY_DECORATOR, this);

        init(heatmap.getLayers().getTopLayer().getDecorator().getScale());

    }

    private void init(IColorScale scale) {
        this.drawer = new ColorScaleDrawer(scale);
        setPreferredSize(this.drawer.getSize());
        repaint();
    }

    public void update() {
        drawer.resetZoom();
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
        init(heatmap.getLayers().getTopLayer().getDecorator().getScale());
    }
}
