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

import org.apache.commons.collections.map.MultiValueMap;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeatmapHeaderIntersectionDrawer extends AbstractHeatmapDrawer {


    private final HeatmapHeaderDrawer colDrawer;
    private final HeatmapHeaderDrawer rowDrawer;

    private MultiValueMap headerLegendDrawers;

    private Map<Object, Integer> XCoordinates;
    private Map<Object, Integer> YCoordinates;

    /* Map that contains the header that are responsible for drawing the
    * legend of a header from the other dimension (row,cols) as for example:
    * HeatmapColoredLabelsHeader --> draws for --> HeatmapDataHeatmapHeader */

    private static final Map<Class<?>, Class<?>> headerRelationsMap = new HashMap<>();

    static {
        headerRelationsMap.put(HeatmapColoredLabelsHeader.class, HeatmapDecoratorHeader.class);
        headerRelationsMap.put(HeatmapTextLabelsHeader.class, HeatmapDecoratorHeader.class);
    }


    public HeatmapHeaderIntersectionDrawer(Heatmap heatmap, HeatmapHeaderDrawer colDrawer, HeatmapHeaderDrawer rowDrawer) {
        super(heatmap);

        this.colDrawer = colDrawer;
        this.rowDrawer = rowDrawer;


        updateDrawers(null);
    }

    private boolean isCompatiblePair(HeatmapHeader thisHeader, HeatmapHeader oppositeHeader) {
        boolean answer = false;
        if (headerRelationsMap.get(thisHeader.getClass()) == oppositeHeader.getClass()) {
            String pattern1 = thisHeader.getAnnotationPattern();
            String pattern2 = oppositeHeader.getAnnotationPattern();
            answer = pattern1 != null && pattern2 != null &&
                    thisHeader.getAnnotationPattern().equals(oppositeHeader.getAnnotationPattern());
        }
        return answer;
    }

    public final void updateDrawers(HeatmapDimension evtSrc) {

        if (getHeatmap().getRows() == evtSrc) {
            rowDrawer.update();
        }

        if (getHeatmap().getColumns() == evtSrc) {
            colDrawer.update();
        }
        getHeaderDrawers();
    }

    private void getHeaderDrawers() {
        XCoordinates = new HashMap<>();
        YCoordinates = new HashMap<>();
        List<AbstractHeatmapHeaderDrawer> colHeaderDrawers = colDrawer.getDrawers();
        List<AbstractHeatmapHeaderDrawer> rowHeaderDrawers = rowDrawer.getDrawers();
        List<HeatmapHeader> rowHeaders = getHeatmap().getRows().getHeaders();
        List<HeatmapHeader> colHeaders = getHeatmap().getColumns().getHeaders();
        headerLegendDrawers = new MultiValueMap();

        int XPosition = 0;
        for (AbstractHeatmapHeaderDrawer d : rowHeaderDrawers) {
            HeatmapHeader thisH = rowHeaders.get(rowHeaderDrawers.indexOf(d));
            XCoordinates.put(d, XPosition);
            XCoordinates.put(thisH, XPosition);
            for (HeatmapHeader oppositeH : colHeaders) {
                if (isCompatiblePair(thisH, oppositeH)) {
                    headerLegendDrawers.put(d, oppositeH);
                }
            }
            XPosition += d.getSize().width;
        }


        int colHeaderSize = 0;
        for (AbstractHeatmapDrawer d : colHeaderDrawers) {
            colHeaderSize += d.getSize().height;
        }
        for (AbstractHeatmapDrawer d : colHeaderDrawers) {
            HeatmapHeader thisH = colHeaders.get(colHeaderDrawers.indexOf(d));
            int size = d.getSize().height;
            colHeaderSize -= size;
            YCoordinates.put(d, colHeaderSize);
            YCoordinates.put(thisH, colHeaderSize);
            for (HeatmapHeader oppositeH : rowHeaders) {
                if (isCompatiblePair(thisH, oppositeH)) {
                    headerLegendDrawers.put(d, oppositeH);
                }
            }
        }
    }


    @Override
    public Dimension getSize() {
        return new Dimension(rowDrawer.getSize().width, colDrawer.getSize().height);
    }

    @Override
    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        Dimension d = getSize();

        int width = d.width < box.width ? d.width : box.width;
        int height = box.height;
        
      /* g.setColor(Color.pink);
        g.fillRect(box.x,box.y,width,height);        */

        drawHeaderIntersection(g, new Rectangle(box.x, box.y, width, height));

    }


    @Override
    public HeatmapPosition getPosition(Point p) {
        return new HeatmapPosition(getHeatmap(), colDrawer.getSize().width, 0);
    }


    @Override
    public Point getPoint(HeatmapPosition p) {
        return new Point(0, 0);
    }


    void drawHeaderIntersection(Graphics2D g, Rectangle headerIntersection) {

        getHeaderDrawers();

        if (headerLegendDrawers == null || headerLegendDrawers.size() == 0) {
            return;
        }

        Set legendDrawers = headerLegendDrawers.keySet();
        for (Object d : legendDrawers) {
            AbstractHeatmapHeaderDrawer drawer = (AbstractHeatmapHeaderDrawer) d;
            Collection headers = headerLegendDrawers.getCollection(d);
            for (Object h : headers) {
                HeatmapHeader header = (HeatmapHeader) h;

                if (!header.isVisible()) {
                    continue;
                }

                int x;
                int y;
                int width;
                int height;
                if (drawer.isHorizontal()) {
                    x = XCoordinates.get(header);
                    y = YCoordinates.get(drawer);
                    width = header.getSize();
                    height = drawer.getSize().height;
                } else {
                    x = XCoordinates.get(drawer);
                    y = YCoordinates.get(header);
                    width = drawer.getSize().width;
                    height = header.getSize();
                }
                Rectangle legendLimits = new Rectangle(x + headerIntersection.x, y + headerIntersection.y, width, height);
                drawer.drawHeaderLegend(g, legendLimits, header);
            }
        }


    }

}
