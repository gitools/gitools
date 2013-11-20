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
package org.gitools.core.heatmap.drawer;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.Decorator;

import java.awt.*;

public class HeatmapBodyDrawer extends AbstractHeatmapDrawer {

    public static final Color SELECTED_COLOR = new Color(0, 0, 128, 100);

    public HeatmapBodyDrawer(Heatmap heatmap) {
        super(heatmap);
    }

    @Override
    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        Heatmap heatmap = getHeatmap();

        calculateFontSize(g, heatmap.getRows().getCellSize(), 8);

        int rowsGridSize = (heatmap.getRows().showGrid() ? heatmap.getRows().getGridSize() : 0);
        int columnsGridSize = (heatmap.getColumns().showGrid() ? heatmap.getColumns().getGridSize() : 0);

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        int cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
        int cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;

        int rowStart = (clip.y - box.y) / cellHeight;
        int rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
        rowEnd = rowEnd < heatmap.getRows().size() ? rowEnd : heatmap.getRows().size();

        int colStart = (clip.x - box.x) / cellWidth;
        int colEnd = (clip.x - box.x + clip.width + cellWidth - 1) / cellWidth;
        colEnd = colEnd < heatmap.getColumns().size() ? colEnd : heatmap.getColumns().size();

        Decorator deco = heatmap.getLayers().getTopLayer().getDecorator();
        Decoration decoration = new Decoration();

        int y = box.y + rowStart * cellHeight;
        for (int row = rowStart; row < rowEnd; row++) {
            int x = box.x + colStart * cellWidth;
            for (int col = colStart; col < colEnd; col++) {

                if (heatmap.isDiagonal() && col < row) {
                    decoration.setBgColor(Color.WHITE);
                    decoration.setValue("");
                } else {
                    decoration.reset();
                    int layerIdx = heatmap.getLayers().getTopLayerIndex();
                    deco.decorate(decoration, heatmap.getLayers().get(layerIdx).getShortFormatter(), heatmap, row, col, layerIdx);
                }

                Color rowsGridColor = heatmap.getRows().getGridColor();
                Color colsGridColor = heatmap.getColumns().getGridColor();

                g.setColor(colsGridColor);
                g.fillRect(x - columnsGridSize, y, columnsGridSize, cellHeight);

                paintCell(decoration, rowsGridColor, rowsGridSize, x - box.x, y - box.y, cellWidth - columnsGridSize, cellHeight - rowsGridSize, g, box);

                x += cellWidth;
            }
            y += cellHeight;
        }


        if (!isPictureMode()) {


            // Draw selected rows
            g.setColor(SELECTED_COLOR);
            int cellSize = heatmap.getRows().getFullSize();
            for (String s : heatmap.getRows().getSelected()) {
                g.fillRect(box.x, box.y + (heatmap.getRows().indexOf(s) * cellSize), box.width, cellSize);
            }

            // Draw row lead
            g.setColor(Color.DARK_GRAY);
            int lead = heatmap.getRows().indexOf(heatmap.getRows().getFocus());
            if (lead != -1) {
                g.fillRect(box.x, box.y + (lead * cellSize) - 1, box.width, 1);
                g.fillRect(box.x, box.y + ((lead + 1) * cellSize) - 1, box.width, 1);
            }

            // Draw selected columns
            g.setColor(SELECTED_COLOR);
            cellSize = heatmap.getColumns().getFullSize();
            for (String s : heatmap.getColumns().getSelected()) {
                g.fillRect(box.x + (heatmap.getColumns().indexOf(s) * cellSize), box.y, cellSize, box.height);
            }

            // Draw column lead
            g.setColor(Color.DARK_GRAY);
            lead = heatmap.getColumns().indexOf(heatmap.getColumns().getFocus());
            if (lead != -1) {
                g.fillRect(box.x + (lead * cellSize) - 1, box.y, 1, box.height);
                g.fillRect(box.x + ((lead + 1) * cellSize) - 1, box.y, 1, box.height);
            }
        }

    }


    @Override
    public Dimension getSize() {

        Heatmap heatmap = getHeatmap();
        int cellWidth = heatmap.getColumns().getFullSize();
        int cellHeight = heatmap.getRows().getFullSize();
        int rowCount = heatmap.getRows().size();
        int columnCount = heatmap.getColumns().size();
        return new Dimension(cellWidth * columnCount, cellHeight * rowCount);
    }


    @Override
    public HeatmapPosition getPosition(Point p) {

        Heatmap heatmap = getHeatmap();

        int cellHeight = heatmap.getRows().getFullSize();
        int rowCount = heatmap.getRows().size();
        int totalHeight = cellHeight * rowCount;

        int cellWidth = heatmap.getColumns().getFullSize();
        int columnCount = heatmap.getColumns().size();
        int totalWidth = cellWidth * columnCount;

        int row = p.y >= 0 && p.y < totalHeight ? p.y / cellHeight : -1;
        int column = p.x >= 0 && p.x < totalWidth ? p.x / cellWidth : -1;

        return new HeatmapPosition(heatmap, row, column);
    }


    @Override
    public Point getPoint(HeatmapPosition p) {

        Heatmap heatmap = getHeatmap();

        int cellHeight = heatmap.getRows().getFullSize();
        int rowCount = heatmap.getRows().size();
        int totalHeight = cellHeight * rowCount;

        int cellWidth = heatmap.getColumns().getFullSize();
        int columnCount = heatmap.getColumns().size();
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
