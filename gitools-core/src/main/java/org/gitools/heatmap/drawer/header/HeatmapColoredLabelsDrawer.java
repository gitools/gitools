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
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class HeatmapColoredLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapColoredLabelsHeader>
{

    private int headerTotalSize = 0;
    private static final double radianAngle90 = (90.0 / 180.0) * Math.PI;


    public HeatmapColoredLabelsDrawer(Heatmap heatmap, HeatmapColoredLabelsHeader header, boolean horizontal)
    {
        super(heatmap, header, horizontal);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip)
    {

        // Clear background
        g.setColor(header.getBackgroundColor());
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        final HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        IMatrixView data = heatmap.getMatrixView();

        g.setFont(header.getLabelFont());
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int fontDescent = fm.getDescent();
        int fontHeight = fm.getHeight() - fontDescent;


        AffineTransform fontAT = new AffineTransform();
        fontAT.rotate(radianAngle90);
        g.setFont(header.getLabelFont().deriveFont(fontAT));
        GenericFormatter gf = new GenericFormatter();

        Color labelColor = header.getLabelColor();

        int clusterYStart = -1;
        int clusterYEnd = -1;
        String legend = null;


        Color gridColor = hdim.getGridColor();
        int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

        int maxWidth = clip.width;
        int width = header.getSize();
        int height = (horizontal ? heatmap.getCellWidth() : heatmap.getCellHeight()) + gridSize;

        width = width < maxWidth ? maxWidth : width;

        int clipStart = clip.y - box.y;
        int clipEnd = clipStart + clip.height;
        int count = horizontal ? data.getColumns().size() : data.getRows().size();

        int start = (clipStart - height) / height;
        int end = (clipEnd + height - 1) / height;

        start = start > 0 ? start : 0;
        end = end < count ? end : count;

        int fontOffset = ((width - fontHeight) / 2) + fm.getDescent();

        int leadRow = data.getLeadSelectionRow();
        int leadColumn = data.getLeadSelectionColumn();

        LabelProvider labelProvider = null;

        if (horizontal)
        {
            labelProvider = new MatrixColumnsLabelProvider(heatmap.getMatrixView());
        }
        else
        {
            labelProvider = new MatrixRowsLabelProvider(heatmap.getMatrixView());
        }

        ColoredLabel lastCluster = null;

        int x = box.x;
        int y = box.y + start * height;
        for (int index = start; index < end; index++)
        {
            Color bgColor = header.getBackgroundColor();
            Color finalGridColor = gridColor;

            String label = labelProvider.getLabel(index);
            ColoredLabel cluster = header.getAssignedColoredLabel(label);
            Color clusterColor = cluster != null ? cluster.getColor() : bgColor;

            if (hdim.isHighlighted(label))
            {
                bgColor = highlightingColor;
            }

            boolean selected = !pictureMode && (horizontal ? data.isColumnSelected(index) : data.isRowSelected(index));

            if (selected)
            {
                bgColor = bgColor.darker();
                clusterColor = clusterColor.darker();
                finalGridColor = gridColor.darker();
            }

            boolean lead = !pictureMode && (horizontal ? (leadColumn == index) /*&& (leadRow == -1)*/ : (leadRow == index) /*&& (leadColumn == -1)*/);

            g.setColor(bgColor);
            g.fillRect(x, y, width, height - gridSize);

            g.setColor(finalGridColor);
            g.fillRect(x, y + height - gridSize, width, gridSize);

            if (cluster != null)
            {
                int sepSize = 0;
                boolean clusterChanged = lastCluster == null || !cluster.equals(lastCluster);
                if (header.isSeparationGrid() && lastCluster != null && clusterChanged)
                {
                    sepSize = gridSize;
                }

                //int thickness = header.getThickness();
                int thickness = header.getSize() - header.getMargin();
                if (thickness < 1)
                {
                    thickness = 1;
                }

                g.setColor(clusterColor);
                g.fillRect(x + header.getMargin(), y + sepSize - gridSize, thickness, height - sepSize);

                // legend
                if (clusterChanged && header.isLabelVisible() && width >= fontHeight + fontDescent)
                {
                    if (clusterYEnd > 0 && clusterYStart >= 0)
                    {
                        paintLegend(g, fm, fontAT, gf, labelColor, clusterYStart, clusterYEnd, legend, fontOffset, x);
                    }

                    clusterYStart = y;
                    clusterYEnd = y + height;
                    legend = cluster.getDisplayedLabel();
                }
                else
                {
                    clusterYEnd = y + height;
                }
            }

            if (lead)
            {
                g.setColor(ColorUtils.invert(bgColor));
                g.drawRect(x, y, width, height - gridSize - 1);
            }

            lastCluster = cluster;

            y += height;
        }

        //last legend
        if (clusterYEnd > 0 && clusterYStart >= 0)
        {
            paintLegend(g, fm, fontAT, gf, labelColor, clusterYStart, clusterYEnd, legend, fontOffset, x);
        }
    }

    private void paintLegend(@NotNull Graphics2D g, @NotNull FontMetrics fm, AffineTransform fontAT, @NotNull GenericFormatter gf, Color labelColor, int clusterYStart, int clusterYEnd, String legend, int fontOffset, int x)
    {
        String formattedLegend = gf.format(legend);
        int stringWidth = fm.stringWidth(formattedLegend);
        int clusterWidth = clusterYEnd - clusterYStart;
        if (stringWidth < clusterWidth)
        {
            int fontYOffset = (clusterWidth - stringWidth) / 2;
            g.setColor(header.isForceLabelColor() ? labelColor : ColorUtils.invert(g.getColor()));
            g.setFont(header.getLabelFont().deriveFont(fontAT));
            g.drawString(legend, x + fontOffset, clusterYStart + fontYOffset);
        }
    }

    @NotNull
    @Override
    public Dimension getSize()
    {
        HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

        int extBorder = /*2 * 1 - 1*/ 0;

        HeatmapDimension dim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        headerTotalSize = header.getSize();

        if (horizontal)
        {
            int cellWidth = heatmap.getCellWidth() + gridSize;
            int columnCount = heatmap.getMatrixView().getColumns().size();
            return new Dimension(cellWidth * columnCount + extBorder, headerTotalSize);
        }
        else
        {
            int cellHeight = heatmap.getCellHeight() + gridSize;
            int rowCount = heatmap.getMatrixView().getRows().size();
            return new Dimension(headerTotalSize, cellHeight * rowCount + extBorder);
        }
    }

    @NotNull
    @Override
    public HeatmapPosition getPosition(@NotNull Point p)
    {
        HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

        int row = -1;
        int col = -1;

        if (horizontal)
        {
            int cellSize = heatmap.getCellWidth() + gridSize;
            int totalSize = cellSize * heatmap.getMatrixView().getColumns().size();
            if (p.x >= 0 && p.x < totalSize)
            {
                col = p.x / cellSize;
            }
        }
        else
        {
            int cellSize = heatmap.getCellHeight() + gridSize;
            int totalSize = cellSize * heatmap.getMatrixView().getRows().size();
            if (p.y >= 0 && p.y < totalSize)
            {
                row = p.y / cellSize;
            }
        }

        return new HeatmapPosition(row, col);
    }

    @NotNull
    @Override
    public Point getPoint(@NotNull HeatmapPosition p)
    {
        HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

        int x = 0;
        int y = 0;

        if (horizontal)
        {
            int cellSize = heatmap.getCellWidth() + gridSize;
            int totalSize = cellSize * heatmap.getMatrixView().getColumns().size();
            x = p.column >= 0 ? p.column * cellSize : 0;
            if (x > totalSize)
            {
                x = totalSize;
            }
        }
        else
        {
            int cellSize = heatmap.getCellHeight() + gridSize;
            int totalSize = cellSize * heatmap.getMatrixView().getRows().size();
            y = p.row >= 0 ? p.row * cellSize : 0;
            if (y > totalSize)
            {
                y = totalSize;
            }
        }

        return new Point(x, y);
    }

    @Override
    public void drawHeaderLegend(@NotNull Graphics2D g, @NotNull Rectangle rect, @NotNull HeatmapHeader oppositeHeatmapHeader)
    {
        int gridSize;
        int height;
        int width;
        int margin;
        int oppositeMargin;

        int y = horizontal ? rect.y : rect.y + rect.height;
        int x = rect.x;


        String[] annValues = oppositeHeatmapHeader.getAnnotationValues(horizontal);
        ColoredLabel[] clusters = header.getClusters();

        gridSize = 1;
        oppositeMargin = oppositeHeatmapHeader.getMargin();
        margin = header.getMargin();
        height = (oppositeHeatmapHeader.getSize() - oppositeMargin - gridSize * annValues.length) / annValues.length;
        width = header.getSize() - margin;

        for (String v : annValues)
        {
            for (ColoredLabel cl : clusters)
            {
                if (cl.getValue().equals(v))
                {
                    // paint
                    g.setColor(cl.getColor());

                    if (horizontal)
                    {

                        g.fillRect(x + oppositeMargin, y, height, width);
                        x += gridSize + height;
                    }
                    else
                    {
                        y -= height;
                        g.fillRect(x, y - oppositeMargin, width, height);
                        y -= gridSize;
                    }
                }
            }
        }
    }
}
