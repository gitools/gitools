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
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.utils.color.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.font.LineMetrics;


public class HeatmapTextLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapTextLabelsHeader>
{

    protected static class AnnotationProvider implements LabelProvider
    {

        private final LabelProvider labelProvider;
        @Nullable
        private final AnnotationMatrix am;
        private final String name;
        private int column;

        public AnnotationProvider(LabelProvider labelProvider, @Nullable AnnotationMatrix am, String name)
        {
            this.labelProvider = labelProvider;
            this.am = am;
            this.name = name;
            if (am != null)
            {
                this.column = am.internalColumnIndex(name);
            }

        }

        @Override
        public int getCount()
        {
            return labelProvider.getCount();
        }


        @Override
        public String getLabel(int index)
        {
            if (am == null)
            {
                return name;
            }

            String label = labelProvider.getLabel(index);
            int row = am.internalRowIndex(label);
            if (row == -1)
            {
                return "";
            }

            if (column == -1)
            {
                return "";
            }

            return am.getCellAsString(row, column);
        }
    }

    public HeatmapTextLabelsDrawer(Heatmap heatmap, HeatmapTextLabelsHeader header, boolean horizontal)
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
        IMatrixView data = heatmap  ;

        g.setFont(header.getFont());

        Color gridColor = hdim.getGridColor();

        int gridSize = hdim.getGridSize();

        int maxWidth = clip.width;
        int width = header.getSize();
        int cellHeight = horizontal ? heatmap.getColumns().getCellSize() : heatmap.getRows().getCellSize();
        int height = cellHeight + gridSize;

        width = width < maxWidth ? maxWidth : width;

        int clipStart = clip.y - box.y;
        int clipEnd = clipStart + clip.height;
        int count = horizontal ? data.getColumns().size() : data.getRows().size();

        int start = (clipStart - height) / height;
        int end = (clipEnd + height - 1) / height;

        start = start > 0 ? start : 0;
        end = end < count ? end : count;

        LineMetrics lm = header.getFont().getLineMetrics("yÍ;|", g.getFontRenderContext());
        float fontHeight = lm.getHeight();
        float fontDesc = lm.getDescent();
        int fontOffset = (int) Math.ceil(((fontHeight + cellHeight) / 2) - fontDesc);

        int leadRow = data.getRows().getSelectionLead(  );
        int leadColumn = data.getColumns().getSelectionLead(  );

        LabelProvider matrixLabelProvider = null;

        if (horizontal)
        {
            matrixLabelProvider = new MatrixColumnsLabelProvider(heatmap  );
        }
        else
        {
            matrixLabelProvider = new MatrixRowsLabelProvider(heatmap  );
        }

        LabelProvider labelProvider = null;
        switch (header.getLabelSource())
        {
            case ID:
                labelProvider = matrixLabelProvider;
                break;
            case ANNOTATION:
                labelProvider = new AnnotationProvider(matrixLabelProvider, hdim.getAnnotations(), header.getLabelAnnotation());
                break;
            case PATTERN:
                labelProvider = new AnnotationsPatternProvider(matrixLabelProvider, hdim.getAnnotations(), header.getLabelPattern());
                break;
        }

        int x = box.x;
        int y = box.y + start * height;
        int padding = 2;
        for (int index = start; index < end; index++)
        {
            Color bgColor = header.getBackgroundColor();
            Color fgColor = header.getLabelColor();
            Color gColor = gridColor;

            String label = labelProvider.getLabel(index);
            String matrixLabel = matrixLabelProvider.getLabel(index);
            if (hdim.isHighlighted(matrixLabel))
            {
                bgColor = highlightingColor;
            }

            boolean selected = !pictureMode && (horizontal ? data.getColumns().isSelected(  index) : data.getRows().isSelected(  index));

            if (selected)
            {
                bgColor = bgColor.darker();
                fgColor = fgColor.darker();
                gColor = gridColor.darker();
            }

            boolean lead = !pictureMode && (horizontal ? (leadColumn == index) /*&& (leadRow == -1)*/ : (leadRow == index) /*&& (leadColumn == -1)*/);

            g.setColor(gColor);
            g.fillRect(x, y + cellHeight, width, gridSize);

            g.setColor(bgColor);
            g.fillRect(x, y, width, cellHeight);

            if (lead)
            {
                g.setColor(ColorUtils.invert(bgColor));
                g.drawRect(x, y, width, cellHeight - 1);
            }

            if (fontHeight <= cellHeight)
            {
                g.setColor(fgColor);
                g.drawString(label, x + padding, y + fontOffset);
            }

            y += height;
        }
    }

    @NotNull
    @Override
    public Dimension getSize()
    {
        HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        int gridSize = hdim.getGridSize();
        int extBorder = /*2 * 1 - 1*/ 0;

        if (horizontal)
        {
            int cellWidth = heatmap.getColumns().getCellSize() + gridSize;
            int columnCount = heatmap  .getColumns().size();
            int headerSize = header.getSize();
            return new Dimension(cellWidth * columnCount + extBorder, headerSize /*- colorAnnSize*/);
        }
        else
        {
            int cellHeight = heatmap.getRows().getCellSize() + gridSize;
            int rowCount = heatmap  .getRows().size();
            int headerSize = header.getSize();
            return new Dimension(headerSize, cellHeight * rowCount + extBorder);
        }
    }

    @NotNull
    @Override
    public HeatmapPosition getPosition(@NotNull Point p)
    {
        HeatmapDimension hdim = horizontal ? heatmap.getColumns() : heatmap.getRows();
        int gridSize = hdim.getGridSize();

        int row = -1;
        int col = -1;

        if (horizontal)
        {
            int cellSize = heatmap.getColumns().getCellSize() + gridSize;
            int totalSize = cellSize * heatmap  .getColumns().size();
            if (p.x >= 0 && p.x < totalSize)
            {
                col = p.x / cellSize;
            }
        }
        else
        {
            int cellSize = heatmap.getRows().getCellSize() + gridSize;
            int totalSize = cellSize * heatmap  .getRows().size();
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
        int gridSize = hdim.getGridSize();

        int x = 0;
        int y = 0;

        if (horizontal)
        {
            int cellSize = heatmap.getColumns().getCellSize() + gridSize;
            int totalSize = cellSize * heatmap  .getColumns().size();
            x = p.column >= 0 ? p.column * cellSize : 0;
            if (x > totalSize)
            {
                x = totalSize;
            }
        }
        else
        {
            int cellSize = heatmap.getRows().getCellSize() + gridSize;
            int totalSize = cellSize * heatmap  .getRows().size();
            y = p.row >= 0 ? p.row * cellSize : 0;
            if (y > totalSize)
            {
                y = totalSize;
            }
        }

        return new Point(x, y);
    }
}
