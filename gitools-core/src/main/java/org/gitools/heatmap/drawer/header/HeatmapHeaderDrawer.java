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
package org.gitools.heatmap.drawer.header;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
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

        drawers = new ArrayList<AbstractHeatmapHeaderDrawer>(headers.size());

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
            for (AbstractHeatmapDrawer d : drawers) {
                Dimension sz = d.getSize();
                Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
                d.draw(g, box2, clip.intersection(box2));
                x += sz.width;
            }
        }
    }

    @Override
    public HeatmapPosition getPosition(@NotNull Point p) {
        int x = 0;
        int y = 0;
        if (isHorizontal()) {
            for (AbstractHeatmapDrawer d : drawers) {
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

    @NotNull
    @Override
    public Point getPoint(HeatmapPosition p) {
        return new Point(0, 0);
    }

    @Override
    public void setPictureMode(boolean pictureMode) {
        for (AbstractHeatmapDrawer d : drawers)
            d.setPictureMode(pictureMode);
    }

    public List<AbstractHeatmapHeaderDrawer> getDrawers() {
        return drawers;
    }
}
