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
package org.gitools.core.heatmap.drawer.header;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.core.label.LabelProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.LineMetrics;


public class HeatmapTextLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapTextLabelsHeader> {

    public HeatmapTextLabelsDrawer(Heatmap heatmap, HeatmapDimension dimension, HeatmapTextLabelsHeader header) {
        super(heatmap, dimension, header);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip) {

        HeatmapTextLabelsHeader header = getHeader();

        final HeatmapDimension heatmapDimension = getHeatmapDimension();

        g.setFont(header.getFont());

        Color gridColor = heatmapDimension.getGridColor();

        int gridSize = (heatmapDimension.showGrid() ? heatmapDimension.getGridSize() : 0);
        int maxWidth = clip.width;
        int width = header.getSize();
        int cellHeight = heatmapDimension.getCellSize();
        int height = cellHeight + gridSize;

        width = width < maxWidth ? maxWidth : width;

        int clipStart = clip.y - box.y;
        int clipEnd = clipStart + clip.height;
        int count = heatmapDimension.size();

        int start = (clipStart - height) / height;
        setFirstIndex(start);

        int end = (clipEnd + height - 1) / height;

        start = start > 0 ? start : 0;
        end = end < count ? end : count;

        LineMetrics lm = header.getFont().getLineMetrics("yÍ;|", g.getFontRenderContext());
        float fontHeight = lm.getHeight();

        float fontSize = g.getFont().getSize();
        while (fontHeight > cellHeight && fontSize > 1) {
            fontSize--;
            g.setFont(g.getFont().deriveFont(fontSize));
            lm = g.getFont().getLineMetrics("yÍ;|", g.getFontRenderContext());
            fontHeight = lm.getHeight();
        }

        float fontDesc = lm.getDescent();
        int fontOffset = (int) Math.ceil(((fontHeight + cellHeight) / 2) - fontDesc);



        LabelProvider labelProvider = header.getLabelProvider();

        int x = box.x;
        int y = box.y + start * height;
        int padding = 5;
        for (int index = start; index < end; index++) {
            Color bgColor = header.getBackgroundColor();
            Color fgColor = header.getLabelColor();
            Color gColor = gridColor;

            String label = labelProvider.getLabel(index);
            String matrixLabel = heatmapDimension.getLabel(index);
            if (heatmapDimension.isHighlighted(matrixLabel)) {
                bgColor = highlightingColor;
            }

            g.setColor(gColor);
            g.fillRect(x, y + cellHeight, width, gridSize);

            g.setColor(bgColor);
            g.fillRect(x, y, width, cellHeight);

            g.setColor(fgColor);
            g.drawString(label, x + padding, y + fontOffset);

            y += height;
        }
    }

}
