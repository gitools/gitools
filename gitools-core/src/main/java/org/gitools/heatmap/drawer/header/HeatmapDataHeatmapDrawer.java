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
import org.gitools.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;

public class HeatmapDataHeatmapDrawer extends AbstractHeatmapHeaderDrawer<HeatmapDataHeatmapHeader>
{

    public HeatmapDataHeatmapDrawer(Heatmap heatmap, HeatmapDataHeatmapHeader header, boolean horizontal)
    {
        super(heatmap, header, horizontal);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip)
    {
        Heatmap headerHeatmap = header.getHeaderHeatmap();
        IMatrixView headerData = header.getHeaderHeatmap();
        IMatrixView data = heatmap  ;


        g.setColor(header.getBackgroundColor());
        g.fillRect(box.x, box.y, box.width, box.height);

        int rowsGridSize;
        int columnsGridSize;
        int cellWidth;
        int cellHeight;
        int rowStart;
        int rowEnd;
        int colStart;
        int colEnd;
        int headerSize;
        int margin;

        margin = header.getMargin();
        int legendPadding = 4;
        Map<String, Integer> labelIndexMap = header.getLabelIndexMap();
        int largestLegendLength = header.isLabelVisible() ? header.getLargestLabelLength() : 0;

        Font font = header.getFont();
        HeatmapDataHeatmapHeader.LabelPositionEnum labelPosition = header.getLabelPosition();

        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(header.getFont());
        GenericFormatter gf = new GenericFormatter();

        // legend will not fit even though user asked for it
        if (header.isLabelVisible() && header.getSize() <= largestLegendLength + legendPadding)
        {
            largestLegendLength = 0;
        }

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        headerSize = header.getSize();

        columnsGridSize = headerHeatmap.getColumns().isGridEnabled() ? headerHeatmap.getColumns().getGridSize() : 0;

        if (horizontal)
        {

            rowsGridSize = heatmap.getColumns().isGridEnabled() ? heatmap.getColumns().getGridSize() : 0;

            cellHeight = heatmap.getColumns().getCellSize() + rowsGridSize;

            colStart = (clip.y - box.y) / cellHeight;
            colEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            colEnd = colEnd < data.getColumns().size() ? colEnd : data.getColumns().size();

            rowStart = (clip.x - box.x) / headerSize;
            rowEnd = headerData.getRows().size();

            cellWidth = (headerSize - margin - columnsGridSize * rowEnd) / rowEnd;


        }
        else
        {

            rowsGridSize = heatmap.getRows().isGridEnabled() ? heatmap.getRows().getGridSize() : 0;

            cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;

            rowStart = (clip.y - box.y) / cellHeight;
            rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            rowEnd = rowEnd < data.getRows().size() ? rowEnd : data.getRows().size();

            colStart = (clip.x - box.x) / headerSize;
            colEnd = headerData.getColumns().size();

            cellWidth = (headerSize - margin - columnsGridSize * colEnd) / colEnd;

        }


        ElementDecorator deco = headerHeatmap.getActiveCellDecorator();
        ElementDecoration decoration = new ElementDecoration();

        int leadRow = headerHeatmap.getRows().getSelectionLead(  );
        int leadColumn = headerHeatmap.getColumns().getSelectionLead(  );

        int y = (horizontal) ? box.y + colStart * cellHeight : box.y + rowStart * cellHeight;
        int Yoriginal = y;
        for (int row = rowStart; row < rowEnd; row++)
        {
            int x;
            if (horizontal)
            {
                y = Yoriginal;
                x = box.x + row * (cellWidth + columnsGridSize);
            }
            else
            {
                x = box.x + colStart * (cellWidth + columnsGridSize);
            }

            for (int col = colStart; col < colEnd; col++)
            {

                int headerCol = col;
                int headerRow = row;
                boolean valueExists;

                if (horizontal)
                {
                    String label = data.getColumns().getLabel(col);
                    valueExists = labelIndexMap.containsKey(label) ? true : false;
                    if (valueExists)
                    {
                        headerCol = labelIndexMap.get(label);
                        //headerRow = 0;
                    }
                }
                else
                {
                    String label = data.getRows().getLabel(row);
                    valueExists = labelIndexMap.containsKey(label) ? true : false;
                    if (valueExists)
                    {
                        headerRow = labelIndexMap.get(label);
                        //headerCol = 0;
                    }
                }

                if (valueExists)
                {

                    deco.decorate(decoration, headerData, headerRow, headerCol);

                    Color color = decoration.getBgColor();
                    Color rowsGridColor = heatmap.getRows().getGridColor();
                    Color columnsGridColor = heatmap.getColumns().getGridColor();

                    boolean selected = false;
                    if (horizontal)
                    {
                        selected = data.getColumns().isSelected(  col) && !pictureMode;
                    }
                    else
                    {
                        selected = !pictureMode && data.getRows().isSelected(  row);
                    }


                    if (selected)
                    {
                        color = color.darker();
                        rowsGridColor = rowsGridColor.darker();
                        columnsGridColor = columnsGridColor.darker();
                    }

                    int colorRectWith = cellWidth;
                    int colorRectX = horizontal ? box.x + margin + (row * (cellWidth + columnsGridSize)) : box.x + margin + (col * (cellWidth + columnsGridSize));
                    int legendStart = x + margin;
                    String valueLabel = gf.format(headerData.getCellValue(headerRow, headerCol, deco.getValueIndex()));
                    int legendLength = fm.stringWidth(valueLabel);


                    if (header.isLabelVisible())
                    {
                        switch (labelPosition)
                        {
                            case rightOf:
                                colorRectWith = cellWidth - legendPadding - largestLegendLength;
                                legendStart = x + legendPadding + margin + colorRectWith;
                                break;

                            case leftOf:
                                colorRectWith = cellWidth - legendPadding - largestLegendLength;
                                colorRectX = x + margin + legendPadding + largestLegendLength;
                                legendStart = legendStart + (largestLegendLength - legendLength);
                                break;

                            case inside:
                                legendStart = x + margin + cellWidth / 2 - largestLegendLength / 2;
                                break;
                        }
                    }


                    g.setColor(color);

                    g.fillRect(colorRectX, y, colorRectWith, cellHeight - rowsGridSize);

                    //g.setColor(rowsGridColor);

                    //g.fillRect(x, y + cellHeight - rowsGridSize, cellWidth, rowsGridSize);

                    //g.setColor(columnsGridColor);

                    //g.fillRect(x + cellWidth - columnsGridSize, y, columnsGridSize, cellWidth - columnsGridSize);


                    if (largestLegendLength > 0)
                    {
                        if (labelPosition != HeatmapDataHeatmapHeader.LabelPositionEnum.inside || header.isForceLabelColor())
                        {
                            g.setColor(header.getLabelColor());
                        }
                        else
                        {
                            g.setColor(ColorUtils.invert(color));
                        }
                        int fontHeight = g.getFontMetrics().getHeight();
                        g.drawString(valueLabel, legendStart, y + fontHeight);
                    }

                    if (!pictureMode)
                    {
                        if (row == leadRow && col == leadColumn)
                        {
                            g.setColor(ColorUtils.invert(color));
                            g.drawRect(x, y, cellWidth - 1, cellHeight - rowsGridSize - 1);
                        }
                        else if (row == leadRow && col != leadColumn)
                        {
                            g.setColor(ColorUtils.invert(color));
                            int x2 = x + cellWidth - 1;
                            int y2 = y + cellHeight - rowsGridSize - 1;
                        }
                        else if (row != leadRow && col == leadColumn)
                        {
                            g.setColor(ColorUtils.invert(color));
                            int x2 = x + cellWidth - 1;
                            int y2 = y + cellHeight - rowsGridSize - 1;
                        }
                    }
                }


                if (horizontal)
                {
                    y += cellHeight;
                }
                else
                {
                    x += cellWidth + columnsGridSize;
                }
            }
            if (horizontal)
            {
                x += cellWidth + columnsGridSize;
            }
            else
            {
                y += cellHeight;
            }
        }
    }

    @NotNull
    @Override
    public Dimension getSize()
    {

        int rowsGridSize = heatmap.getRows().isGridEnabled() ? heatmap.getRows().getGridSize() : 0;
        int columnsGridSize = heatmap.getColumns().isGridEnabled() ? heatmap.getColumns().getGridSize() : 0;
        int rowCount = heatmap  .getRows().size();
        int columnCount = heatmap  .getColumns().size();
        int extBorder = /*2 * 1 - 1*/ 0;


        int totalWidth;
        int totalHeight;
        if (horizontal)
        {
            totalWidth = (heatmap.getColumns().getCellSize() + columnsGridSize) * columnCount + extBorder;
            totalHeight = header.getSize();

        }
        else
        {
            totalWidth = header.getSize();
            totalHeight = (heatmap.getRows().getCellSize() + rowsGridSize) * rowCount + extBorder;
        }
        return new Dimension(totalWidth, totalHeight);
    }

    @NotNull
    @Override
    public HeatmapPosition getPosition(@NotNull Point p)
    {

        int rowsGridSize = heatmap.getRows().isGridEnabled() ? heatmap.getRows().getGridSize() : 0;
        int columnsGridSize = heatmap.getColumns().isGridEnabled() ? heatmap.getColumns().getGridSize() : 0;
        int extBorder = /*2 * 1 - 1*/ 0;

        int totalWidth;
        int totalHeight;
        int cellWidth;
        int cellHeight;

        int rowCount = heatmap  .getRows().size();
        int columnCount = heatmap  .getColumns().size();

        if (horizontal)
        {
            cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
            cellHeight = header.getSize();
            totalWidth = cellWidth * columnCount + extBorder;
            totalHeight = header.getSize();
        }
        else
        {
            cellWidth = header.getSize();
            cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;
            totalWidth = header.getSize();
            totalHeight = cellHeight * rowCount + extBorder;
        }

        int row = p.y >= 0 && p.y < totalHeight ? (p.y - extBorder) / cellHeight : -1;
        int column = p.x >= 0 && p.x < totalWidth ? (p.x - extBorder) / cellWidth : -1;

        return new HeatmapPosition(row, column);
    }

    @NotNull
    @Override
    public Point getPoint(@NotNull HeatmapPosition p)
    {

        int rowsGridSize = heatmap.getRows().isGridEnabled() ? heatmap.getRows().getGridSize() : 0;
        int columnsGridSize = heatmap.getColumns().isGridEnabled() ? heatmap.getColumns().getGridSize() : 0;
        int extBorder = /*2 * 1 - 1*/ 0;

        int totalWidth;
        int totalHeight;
        int cellWidth;
        int cellHeight;

        int rowCount = heatmap  .getRows().size();
        int columnCount = heatmap  .getColumns().size();

        if (horizontal)
        {
            cellWidth = heatmap.getColumns().getCellSize() + columnsGridSize;
            cellHeight = header.getSize();
            totalWidth = cellWidth * columnCount + extBorder;
            totalHeight = header.getSize();
        }
        else
        {
            cellWidth = header.getSize();
            cellHeight = heatmap.getRows().getCellSize() + rowsGridSize;
            totalWidth = header.getSize();
            totalHeight = cellHeight * rowCount + extBorder;
        }

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