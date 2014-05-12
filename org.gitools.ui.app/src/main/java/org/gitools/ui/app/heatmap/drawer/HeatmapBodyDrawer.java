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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;

import java.awt.*;

public class HeatmapBodyDrawer extends AbstractHeatmapDrawer {

    public HeatmapBodyDrawer(Heatmap heatmap) {
        super(heatmap);
    }

    @Override
    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        Application.get().setCursorWaiting();


        g.setFont(heatmap.getLayers().getTopLayer().getFont());
        calculateFontSize(g, rows.getCellSize(), 7);

        int rowsGridSize = (rows.showGrid() ? rows.getGridSize() : 0);
        int columnsGridSize = (columns.showGrid() ? columns.getGridSize() : 0);

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        int cellWidth = columns.getCellSize() + columnsGridSize;
        int cellHeight = rows.getCellSize() + rowsGridSize;

        int rowStart = (clip.y - box.y) / cellHeight;
        int rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
        rowEnd = rowEnd < rows.size() ? rowEnd : rows.size();

        int colStart = (clip.x - box.x) / cellWidth;
        int colEnd = (clip.x - box.x + clip.width + cellWidth - 1) / cellWidth;
        colEnd = colEnd < columns.size() ? colEnd : columns.size();

        Decoration decoration = new Decoration();
        int y = box.y + rowStart * cellHeight;
        for (int row = rowStart; row < rowEnd; row++) {
            int x = box.x + colStart * cellWidth;
            for (int col = colStart; col < colEnd; col++) {

                if (heatmap.isDiagonal() && col < row) {
                    decoration.setBgColor(Color.WHITE);
                    decoration.setValue("");
                } else {
                    decorateCell(decoration.reset(), row, col);
                }

                Color rowsGridColor = rows.getGridColor();
                Color colsGridColor = columns.getGridColor();

                g.setColor(colsGridColor);
                g.fillRect(x - columnsGridSize, y, columnsGridSize, cellHeight);

                paintCell(decoration, rowsGridColor, rowsGridSize, x - box.x, y - box.y, cellWidth - columnsGridSize, cellHeight - rowsGridSize, g, box);

                x += cellWidth;
            }
            y += cellHeight;
        }


        if (!isPictureMode()) {
            drawSelectedAndFocus(g, box, rows, true);
            drawSelectedAndFocus(g, box, columns, false);
        }

        Application.get().setCursorNormal();

    }

    protected void decorateCell(Decoration decoration, int row, int col) {

        HeatmapLayer topLayer = heatmap.getLayers().getTopLayer();
        Decorator decorator = topLayer.getDecorator();

        String rowId = heatmap.getRows().getLabel(row);
        String columnId = heatmap.getColumns().getLabel(col);

        decorator.decorate(decoration, topLayer.getShortFormatter(), heatmap, topLayer, rowId, columnId);
    }


    @Override
    public Dimension getSize() {
        return new Dimension(columns.getFullSize() * columns.size(), rows.getFullSize() * rows.size());
    }


    @Override
    public HeatmapPosition getPosition(Point p) {

        int cellHeight = rows.getFullSize();
        int rowCount = rows.size();
        int totalHeight = cellHeight * rowCount;

        int cellWidth = columns.getFullSize();
        int columnCount = columns.size();
        int totalWidth = cellWidth * columnCount;

        int row = p.y >= 0 && p.y < totalHeight ? p.y / cellHeight : -1;
        int column = p.x >= 0 && p.x < totalWidth ? p.x / cellWidth : -1;

        return new HeatmapPosition(heatmap, row, column);
    }


    @Override
    public Point getPoint(HeatmapPosition p) {

        int cellHeight = rows.getFullSize();
        int rowCount = rows.size();
        int totalHeight = cellHeight * rowCount;

        int cellWidth = columns.getFullSize();
        int columnCount = columns.size();
        int totalWidth = cellWidth * columnCount;

        int x = p.column >= 0 ? p.column * cellWidth : 0;
        if (x > totalWidth) {
            x = totalWidth;
        }

        int y = p.row >= 0 ? p.row * cellHeight : 0;
        if (y > totalHeight) {
            y = totalHeight;
        }

        return new Point(x, y);
    }
}
