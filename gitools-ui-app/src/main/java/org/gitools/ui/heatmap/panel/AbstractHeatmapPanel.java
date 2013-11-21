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
package org.gitools.ui.heatmap.panel;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.ui.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.ui.heatmap.drawer.header.HeatmapHeaderDrawer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.gitools.utils.events.EventUtils.isAny;

class AbstractHeatmapPanel extends JPanel implements PropertyChangeListener {

    private final AbstractHeatmapDrawer drawer;

    AbstractHeatmapPanel(Heatmap heatmap, AbstractHeatmapDrawer drawer) {
        this.drawer = drawer;
        this.drawer.setHeatmap(heatmap);

        heatmap.getColumns().addPropertyChangeListener(this);
        heatmap.getRows().addPropertyChangeListener(this);
        heatmap.getLayers().addPropertyChangeListener(this);
        heatmap.getLayers().getTopLayer().getDecorator().addPropertyChangeListener(this);

        setBorder(null);
        setBackground(Color.WHITE);
        setPreferredSize(drawer.getSize());

    }

    public AbstractHeatmapDrawer getDrawer() {
        return drawer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension size = drawer.getSize();
        Rectangle box = new Rectangle(0, 0, size.width, size.height);
        Rectangle clip = g.getClipBounds();
        drawer.draw((Graphics2D) g, box, clip);
    }

    public void updateSize() {
        setPreferredSize(drawer.getSize());
        revalidate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (isAny(evt, HeatmapDimension.class, HeatmapDimension.PROPERTY_HEADERS)) {
            if (getDrawer() instanceof HeatmapHeaderDrawer) {
                ((HeatmapHeaderDrawer) getDrawer()).update();
            }
        }

        if (isAny(evt, HeatmapDimension.class) ||
                isAny(evt, Decorator.class) ||
                isAny(evt, HeatmapLayers.class)
                ) {
            updateSize();
            repaint();
        }
    }
}
