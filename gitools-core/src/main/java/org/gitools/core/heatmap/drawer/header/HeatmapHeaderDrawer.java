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
package org.gitools.core.heatmap.drawer.header;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.core.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.core.heatmap.drawer.HeatmapBodyDrawer;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.utils.collections.ReverseListIterator;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeatmapHeaderDrawer extends AbstractHeatmapDrawer {

    private HeatmapDimension heatmapDimension;

    private List<AbstractHeatmapHeaderDrawer> drawers;

    public HeatmapHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension) {
        super(heatmap);
        this.heatmapDimension = heatmapDimension;
        update();
    }

    public final void update() {
        List<HeatmapHeader> headers = heatmapDimension.getHeaders();

        drawers = new ArrayList<>(headers.size());

        for (int i = 0; i < headers.size(); i++) {
            HeatmapHeader h = headers.get(i);
            if (!h.isVisible()) {
                continue;
            }

            AbstractHeatmapHeaderDrawer d = null;
            if (h instanceof HeatmapTextLabelsHeader) {
                d = new HeatmapTextLabelsDrawer(getHeatmap(), heatmapDimension, (HeatmapTextLabelsHeader) h);
            } else if (h instanceof HeatmapColoredLabelsHeader) {
                d = new HeatmapColoredLabelsDrawer(getHeatmap(), heatmapDimension, (HeatmapColoredLabelsHeader) h);
            } else if (h instanceof HeatmapDecoratorHeader) {
                d = new HeatmapDecoratorHeaderDrawer(getHeatmap(), heatmapDimension, (HeatmapDecoratorHeader) h);
            }

            if (d != null) {
                d.setPictureMode(isPictureMode());
                drawers.add(d);
            }
        }
    }

    @Deprecated
    private boolean isHorizontal() {
        return getHeatmap().getColumns() == heatmapDimension;
    }

    @NotNull
    @Override
    public Dimension getSize() {
        int w = 0;
        int h = 0;
        if (isHorizontal()) {
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                if (sz.width > w) {
                    w = sz.width;
                }
                h += sz.height;
            }
        } else {
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                if (sz.height > h) {
                    h = sz.height;
                }
                w += sz.width;
            }
        }

        return new Dimension(w, h);
    }

    private static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip) {

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (isHorizontal()) {
            int x = box.y;
            int y = box.x;
            int totalSize = box.height;
            Rectangle clip2 = new Rectangle(clip.y, clip.x, clip.height, clip.width);
            g.rotate(radianAngle90);
            g.translate(-totalSize, 0);
            g.fillRect(box.x, box.y, box.width, box.height);
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.height, sz.width);
                d.draw(g, box2, clip2.intersection(box2));
                x += box2.width;
            }
        } else {
            int x = box.x;
            int y = box.y;

            Dimension sz;
            for (AbstractHeatmapDrawer d : drawers) {
                sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, clip.width - x, sz.height);
                d.draw(g, box2, clip.intersection(box2));
                x += sz.width;
            }
        }

        if (!isPictureMode()) {

            // Draw selected
            g.setColor(HeatmapBodyDrawer.SELECTED_COLOR);
            int cellSize = heatmapDimension.getFullSize();
            for (int s : heatmapDimension.getSelected()) {
                g.fillRect(box.x, box.y + (s * cellSize), box.width, cellSize);
            }

            // Draw row lead
            g.setColor(Color.DARK_GRAY);
            int lead = heatmapDimension.getSelectionLead();
            if (lead != -1) {
                g.fillRect(box.x, box.y + (lead * cellSize) - 1, box.width, 1);
                g.fillRect(box.x, box.y + ((lead+1) * cellSize) - 1, box.width, 1);
            }

        }
    }

    @Override
    public HeatmapPosition getPosition(@NotNull Point p) {
        int x = 0;
        int y = 0;
        if (isHorizontal()) {
            for (AbstractHeatmapDrawer d : new ReverseListIterator<>(drawers)) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    p.translate(-x, -y);
                    return d.getPosition(p);
                }
                y += sz.height;
            }
        } else {
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    p.translate(-x, -y);
                    return d.getPosition(p);
                }
                x += sz.width;
            }
        }
        return new HeatmapPosition(-1, -1);
    }

    public HeatmapHeader getHeader(Point p) {
        int x = 0;
        int y = 0;
        if (isHorizontal()) {

            for (AbstractHeatmapHeaderDrawer d : new ReverseListIterator<>(drawers)) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    return d.getHeader();
                }
                y += sz.height;
            }
        } else {
            for (AbstractHeatmapHeaderDrawer d : drawers) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                if (box2.contains(p)) {
                    return d.getHeader();
                }
                x += sz.width;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Point getPoint(HeatmapPosition p) {
        return new Point(0, 0);
    }

    @Override
    public void setPictureMode(boolean pictureMode) {
        super.setPictureMode(pictureMode);

        for (AbstractHeatmapDrawer d : drawers)
            d.setPictureMode(pictureMode);
    }

    public List<AbstractHeatmapHeaderDrawer> getDrawers() {
        return drawers;
    }
}
