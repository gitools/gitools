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
package org.gitools.ui.app.heatmap.drawer;

import org.gitools.analysis._DEPRECATED.heatmap.Heatmap;
import org.gitools.ui.app.heatmap.drawer.header.HeatmapHeaderDrawer;
import org.gitools.ui.app.heatmap.drawer.header.HeatmapHeaderIntersectionDrawer;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class HeatmapDrawer extends AbstractHeatmapDrawer {

    private final HeatmapBodyDrawer body;

    private final HeatmapHeaderDrawer rowsHeader;

    private final HeatmapHeaderDrawer colsHeader;

    private final HeatmapHeaderIntersectionDrawer headerIntersection;

    public HeatmapDrawer(Heatmap heatmap) {
        super(heatmap);

        body = new HeatmapBodyDrawer(heatmap);
        rowsHeader = new HeatmapHeaderDrawer(heatmap, heatmap.getRows());
        colsHeader = new HeatmapHeaderDrawer(heatmap, heatmap.getColumns());
        headerIntersection = new HeatmapHeaderIntersectionDrawer(heatmap, colsHeader, rowsHeader);
    }

    @Override
    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
        Dimension bodySize = body.getSize();
        Dimension rowsSize = rowsHeader.getSize();
        Dimension columnsSize = colsHeader.getSize();

        Rectangle columnsBounds = new Rectangle(0, 0, columnsSize.width, columnsSize.height);
        Rectangle bodyBounds = new Rectangle(0, columnsSize.height, bodySize.width, bodySize.height);
        Rectangle rowsBounds = new Rectangle(bodySize.width, columnsSize.height, rowsSize.width, rowsSize.height);
        Rectangle headerIntersectionBounds = new Rectangle(bodySize.width, 0, rowsSize.width, columnsSize.height);

        AffineTransform at = new AffineTransform();

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        colsHeader.draw(g, columnsBounds, columnsBounds);
        at.setToIdentity();
        g.setTransform(at);
        body.draw(g, bodyBounds, bodyBounds);
        at.setToIdentity();
        g.setTransform(at);
        rowsHeader.draw(g, rowsBounds, rowsBounds);
        at.setToIdentity();
        g.setTransform(at);
        headerIntersection.draw(g, headerIntersectionBounds, headerIntersectionBounds);

    }


    @Override
    public Dimension getSize() {
        Dimension bodySize = body.getSize();
        Dimension rowsSize = rowsHeader.getSize();
        Dimension columnsSize = colsHeader.getSize();
        return new Dimension(bodySize.width + rowsSize.width, bodySize.height + columnsSize.height);
    }


    @Override
    public HeatmapPosition getPosition(Point p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Point getPoint(HeatmapPosition p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPictureMode(boolean pictureMode) {
        super.setPictureMode(pictureMode);
        body.setPictureMode(pictureMode);
        rowsHeader.setPictureMode(pictureMode);
        colsHeader.setPictureMode(pictureMode);
    }
}
