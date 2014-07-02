/*
 * #%L
 * gitools-core
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
package org.gitools.ui.app.heatmap.drawer.header;

import com.google.common.collect.Lists;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.ui.core.HeatmapPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HierarchicalClusterHeaderDrawer extends AbstractHeatmapHeaderDrawer<HierarchicalClusterHeatmapHeader> {

    private static final double radianAngle90 = (90.0 / 180.0) * Math.PI;
    List<HeatmapColoredLabelsDrawer> drawers;


    public HierarchicalClusterHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HierarchicalClusterHeatmapHeader header) {
        super(heatmap, heatmapDimension, header);
        updateDrawers();
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        HeatmapColoredLabelsDrawer drawer;

        if (drawers.size() != getHeader().getClusterLevels().size()) {
            updateDrawers();
        }

        int x = clip.x;
        int y = clip.y;

        for (HeatmapColoredLabelsHeader level : Lists.reverse(getHeader().getClusterLevels())) {
            int width = level.getSize();
            drawer = drawers.get(getHeader().getClusterLevels().indexOf(level));
            Rectangle subclip = new Rectangle(x, y, width, clip.height);
            Rectangle subbox = new Rectangle(x, box.y, width, box.height);
            drawer.draw(g, subbox, subclip);
            x += width;
        }

    }

    private void updateDrawers() {
        drawers = new ArrayList<>();
        for (HeatmapColoredLabelsHeader levelHeader : getHeader().getClusterLevels()) {
            drawers.add(new HeatmapColoredLabelsDrawer(heatmap, getHeatmapDimension(), levelHeader));
        }
    }

    @Override
    public HeatmapPosition getPosition(Point p) {
        int point = (isHorizontal() ? p.x : p.y);
        int drawerindex = getDrawerIndexFromPoint(p, 0, 0);
        int index = getHeaderPosition(point);
        String identifier = getHeatmapDimension().getLabel(index);
        String label = getHeader().getClusterLevels().get(drawerindex).getColoredLabel(identifier).getValue();

        return (isHorizontal() ? new HeatmapPosition(getHeatmap(), -1, index, label) : new HeatmapPosition(getHeatmap(), index, -1, label));
    }

    @Override
    public Dimension getSize() {
        Dimension oneDrawerSize = drawers.get(0).getSize();
        int total = isHorizontal() ? (int) oneDrawerSize.getWidth() : (int) oneDrawerSize.getHeight();
        Dimension d;
        if (isHorizontal()) {
            d = new Dimension(total, getHeader().getSize());
        } else {
            d = new Dimension(getHeader().getSize(), total);
        }
        return d;
    }


    public int getDrawerIndexFromPoint(Point p, int x, int y) {
        if (isHorizontal()) {
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    return drawers.indexOf(d);
                }
                y += sz.height;
            }
        } else {
            for (AbstractHeatmapDrawer d : Lists.reverse(drawers)) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    return drawers.indexOf(d);
                }
                x += sz.width;
            }
        }
        return 0;
    }


    @Override
    public void configure(Point p, int x, int y) {
        int index = getDrawerIndexFromPoint(p, x, y);
        getHeader().setInteractionLevel(index);
    }

}
