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
import org.gitools.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.model.decorator.Decoration;

import java.awt.*;

public class HeatmapDecoratorHeaderDrawer extends AbstractHeatmapHeaderDrawer<HeatmapDecoratorHeader> {

    public HeatmapDecoratorHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapDecoratorHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        HeatmapDecoratorHeader header = getHeader();

        paintBackground(header.getBackgroundColor(), g, box);

        int firstIndex = firstVisibleIndex(box, clip);
        int lastIndex = lastVisibleIndex(box, clip);

        Decoration decoration = new Decoration();
        for (int index = firstIndex; index <= lastIndex; index++) {
            for (String annotation : header.getAnnotationLabels()) {
                header.decorate(decoration, index, annotation);
                paintCell(decoration, index, g, box, clip);
            }
        }
    }

    /*
    public void deprecatdDraw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip) {

        HeatmapDataHeatmapHeader header = getHeader();
        HeatmapDimension hdim = getHeatmapDimension();

        //IMatrixView headerData = header.getHeatmap();
        //IMatrixView data = getHeatmap();

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
        if (header.isLabelVisible() && header.getSize() <= largestLegendLength + legendPadding) {
            largestLegendLength = 0;
        }

        headerSize = header.getSize();

        columnsGridSize = hdim.getGridSize();

        if (isHorizontal()) {

            rowsGridSize = hdim.getGridSize();

            cellHeight = hdim.getCellSize() + rowsGridSize;

            colStart = (clip.y - box.y) / cellHeight;
            colEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            colEnd = colEnd < hdim.size() ? colEnd : hdim.size();

            rowStart = (clip.x - box.x) / headerSize;
            rowEnd = hdim.size();

            cellWidth = (headerSize - margin - columnsGridSize * rowEnd) / rowEnd;


        } else {

            rowsGridSize = hdim.getGridSize();

            cellHeight = hdim.getCellSize() + rowsGridSize;

            rowStart = (clip.y - box.y) / cellHeight;
            rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            rowEnd = rowEnd < hdim.size() ? rowEnd : hdim.size();

            colStart = (clip.x - box.x) / headerSize;
            colEnd = hdim.size();

            cellWidth = (headerSize - margin - columnsGridSize * colEnd) / colEnd;

        }


        Decorator deco = header.getDecorator();
        Decoration decoration = new Decoration();

        int y = (isHorizontal()) ? box.y + colStart * cellHeight : box.y + rowStart * cellHeight;
        int Yoriginal = y;
        for (int row = rowStart; row < rowEnd; row++) {
            int x;
            if (isHorizontal()) {
                y = Yoriginal;
                x = box.x + row * (cellWidth + columnsGridSize);
            } else {
                x = box.x + colStart * (cellWidth + columnsGridSize);
            }

            for (int col = colStart; col < colEnd; col++) {

                int headerCol = col;
                int headerRow = row;
                boolean valueExists;

                if (isHorizontal()) {
                    String label = hdim.getLabel(col);
                    valueExists = labelIndexMap.containsKey(label) ? true : false;
                    if (valueExists) {
                        headerCol = labelIndexMap.get(label);
                    }
                } else {
                    String label = hdim.getLabel(row);
                    valueExists = labelIndexMap.containsKey(label) ? true : false;
                    if (valueExists) {
                        headerRow = labelIndexMap.get(label);
                    }
                }

                if (valueExists) {

                    deco.decorate(decoration, headerData, headerRow, headerCol, 0);

                    Color color = decoration.getBgColor();

                    boolean selected = !isPictureMode() && hdim.isSelected(col);

                    if (selected) {
                        color = color.darker();
                    }

                    int colorRectWith = cellWidth;
                    int colorRectX = isHorizontal() ? box.x + margin + (row * (cellWidth + columnsGridSize)) : box.x + margin + (col * (cellWidth + columnsGridSize));
                    int legendStart = x + margin;
                    String valueLabel = gf.format(headerData.getCellValue(headerRow, headerCol, 0));
                    int legendLength = fm.stringWidth(valueLabel);


                    if (header.isLabelVisible()) {
                        switch (labelPosition) {
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

                    if (largestLegendLength > 0) {
                        if (labelPosition != HeatmapDataHeatmapHeader.LabelPositionEnum.inside || header.isForceLabelColor()) {
                            g.setColor(header.getLabelColor());
                        } else {
                            g.setColor(ColorUtils.invert(color));
                        }
                        int fontHeight = g.getFontMetrics().getHeight();
                        g.drawString(valueLabel, legendStart, y + fontHeight);
                    }


                }


                if (isHorizontal()) {
                    y += cellHeight;
                } else {
                    x += cellWidth + columnsGridSize;
                }
            }
            if (isHorizontal()) {
                x += cellWidth + columnsGridSize;
            } else {
                y += cellHeight;
            }
        }
    }
    */
}