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
package org.gitools.heatmap.drawer;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.decorator.Decoration;
import org.gitools.model.decorator.Decorator;
import org.gitools.utils.color.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HeatmapBodyDrawer extends AbstractHeatmapDrawer
{

    public HeatmapBodyDrawer(Heatmap heatmap)
    {
        super(heatmap);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip)
    {

        int rowsGridSize = heatmap.getRows().getGridSize();
        int columnsGridSize = heatmap.getColumns().getGridSize();

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        IMatrixView data = heatmap;

        int cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
        int cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;

        //TODO take into account extBorderSize
        int rowStart = (clip.y - box.y) / cellHeight;
        int rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
        rowEnd = rowEnd < data.getRows().size() ? rowEnd : data.getRows().size();

        //TODO take into account extBorderSize
        int colStart = (clip.x - box.x) / cellWidth;
        int colEnd = (clip.x - box.x + clip.width + cellWidth - 1) / cellWidth;
        colEnd = colEnd < data.getColumns().size() ? colEnd : data.getColumns().size();

        Decorator deco = heatmap.getLayers().getTopLayer().getDecorator();
        Decoration decoration = new Decoration();

        int leadRow = heatmap.getRows().getSelectionLead(  );
        int leadColumn = heatmap.getColumns().getSelectionLead(  );

        int y = box.y + rowStart * cellHeight;
        for (int row = rowStart; row < rowEnd; row++)
        {
            int x = box.x + colStart * cellWidth;
            boolean rowSelected = data.getRows().isSelected(row);
            for (int col = colStart; col < colEnd; col++)
            {
                deco.decorate(decoration, data, row, col, data.getLayers().getTopLayerIndex());

                Color color = decoration.getBgColor();
                Color rowsGridColor = heatmap.getRows().getGridColor();
                Color columnsGridColor = heatmap.getColumns().getGridColor();

                boolean selected = !pictureMode && (rowSelected || data.getColumns().isSelected(  col));

                if (selected)
                {
                    color = color.darker();
                    rowsGridColor = rowsGridColor.darker();
                    columnsGridColor = columnsGridColor.darker();
                }

                g.setColor(color);

                g.fillRect(x, y, cellWidth - columnsGridSize, cellHeight - rowsGridSize);

                g.setColor(rowsGridColor);

                g.fillRect(x, y + cellHeight - rowsGridSize, cellWidth, rowsGridSize);

                g.setColor(columnsGridColor);

                g.fillRect(x + cellWidth - columnsGridSize, y, columnsGridSize, cellWidth - columnsGridSize);

                if (!pictureMode)
                {
                    if (row == leadRow && col == leadColumn)
                    {
                        g.setColor(ColorUtils.invert(color));
                        g.drawRect(x, y, cellWidth - columnsGridSize - 1, cellHeight - rowsGridSize - 1);
                    }
                    else if (row == leadRow && col != leadColumn)
                    {
                        g.setColor(ColorUtils.invert(color));
                        int x2 = x + cellWidth - columnsGridSize - 1;
                        int y2 = y + cellHeight - rowsGridSize - 1;
                        g.drawLine(x, y, x2, y);
                        g.drawLine(x, y2, x2, y2);
                    }
                    else if (row != leadRow && col == leadColumn)
                    {
                        g.setColor(ColorUtils.invert(color));
                        int x2 = x + cellWidth - columnsGridSize - 1;
                        int y2 = y + cellHeight - rowsGridSize - 1;
                        g.drawLine(x, y, x, y2);
                        g.drawLine(x2, y, x2, y2);
                    }
                }

                x += cellWidth;
            }
            y += cellHeight;
        }
    }

    @NotNull
    @Override
    public Dimension getSize()
    {
        int rowsGridSize = heatmap.getRows().getGridSize();
        int columnsGridSize = heatmap.getColumns().getGridSize();
        int cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
        int cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;
        int rowCount = heatmap  .getRows().size();
        int columnCount = heatmap  .getColumns().size();
        int extBorder = /*2 * 1 - 1*/ 0;
        return new Dimension(cellWidth * columnCount + extBorder, cellHeight * rowCount + extBorder);
    }

    @NotNull
    @Override
    public HeatmapPosition getPosition(@NotNull Point p)
    {
        int rowsGridSize = heatmap.getRows().getGridSize();
        int columnsGridSize = heatmap.getColumns().getGridSize();
        int extBorder = /*2 * 1 - 1*/ 0;

        int cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;
        int rowCount = heatmap.getRows().size();
        int totalHeight = cellHeight * rowCount + extBorder;

        int cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
        int columnCount = heatmap  .getColumns().size();
        int totalWidth = cellWidth * columnCount + extBorder;

        int row = p.y >= 0 && p.y < totalHeight ? (p.y - extBorder) / cellHeight : -1;
        int column = p.x >= 0 && p.x < totalWidth ? (p.x - extBorder) / cellWidth : -1;

        return new HeatmapPosition(row, column);
    }

    @NotNull
    @Override
    public Point getPoint(@NotNull HeatmapPosition p)
    {
        int rowsGridSize = heatmap.getRows().getGridSize();
        int columnsGridSize = heatmap.getColumns().getGridSize();
        int extBorder = /*2 * 1 - 1*/ 0;

        int cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;
        int rowCount = heatmap  .getRows().size();
        int totalHeight = cellHeight * rowCount + extBorder;

        int cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
        int columnCount = heatmap  .getColumns().size();
        int totalWidth = cellWidth * columnCount + extBorder;

        int x = p.column >= 0 ? p.column * cellWidth : 0;
        if (x > totalWidth)
        {
            x = totalWidth;
        }

        int y = p.row >= 0 ? p.row * cellHeight : 0;
        if (y > totalHeight)
        {
            y = totalHeight;
        }

        return new Point(x, y);
    }
}
