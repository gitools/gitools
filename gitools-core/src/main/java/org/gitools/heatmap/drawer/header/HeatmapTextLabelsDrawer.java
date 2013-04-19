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
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixDimensionLabelProvider;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.utils.color.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.LineMetrics;


public class HeatmapTextLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapTextLabelsHeader> {

    protected static class AnnotationProvider implements LabelProvider {

        private final LabelProvider labelProvider;
        private final IAnnotations am;
        private final String name;

        public AnnotationProvider(LabelProvider labelProvider, IAnnotations am, String name) {
            this.labelProvider = labelProvider;
            this.am = am;
            this.name = name;
        }

        @Override
        public int getCount() {
            return labelProvider.getCount();
        }


        @Override
        public String getLabel(int index) {
            if (am == null) {
                return name;
            }

            String label = labelProvider.getLabel(index);
            String annotation = am.getAnnotation(label, name);

            if (annotation == null) {
                return "";
            }

            return annotation;
        }
    }

    public HeatmapTextLabelsDrawer(Heatmap heatmap, HeatmapDimension dimension, HeatmapTextLabelsHeader header) {
        super(heatmap, dimension, header);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip) {

        HeatmapTextLabelsHeader header = getHeader();

        final HeatmapDimension heatmapDimension = getHeatmapDimension();

        g.setFont(header.getFont());

        Color gridColor = heatmapDimension.getGridColor();

        int gridSize = heatmapDimension.getGridSize();
        int maxWidth = clip.width;
        int width = header.getSize();
        int cellHeight = heatmapDimension.getCellSize();
        int height = cellHeight + gridSize;

        width = width < maxWidth ? maxWidth : width;

        int clipStart = clip.y - box.y;
        int clipEnd = clipStart + clip.height;
        int count = heatmapDimension.size();

        int start = (clipStart - height) / height;
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

        LabelProvider matrixLabelProvider = new MatrixDimensionLabelProvider(heatmapDimension);
        LabelProvider labelProvider = null;
        switch (header.getLabelSource()) {
            case ID:
                labelProvider = matrixLabelProvider;
                break;
            case ANNOTATION:
                labelProvider = new AnnotationProvider(matrixLabelProvider, heatmapDimension.getAnnotations(), header.getLabelAnnotation());
                break;
            case PATTERN:
                labelProvider = new AnnotationsPatternProvider(matrixLabelProvider, heatmapDimension.getAnnotations(), header.getLabelPattern());
                break;
        }

        int x = box.x;
        int y = box.y + start * height;
        int padding = 2;
        for (int index = start; index < end; index++) {
            Color bgColor = header.getBackgroundColor();
            Color fgColor = header.getLabelColor();
            Color gColor = gridColor;

            String label = labelProvider.getLabel(index);
            String matrixLabel = matrixLabelProvider.getLabel(index);
            if (heatmapDimension.isHighlighted(matrixLabel)) {
                bgColor = highlightingColor;
            }

            boolean selected = !isPictureMode() && heatmapDimension.isSelected(index);

            if (selected) {
                bgColor = bgColor.darker();
                fgColor = fgColor.darker();
                gColor = gridColor.darker();
            }

            boolean lead = !isPictureMode() && (heatmapDimension.getSelectionLead() == index);

            g.setColor(gColor);
            g.fillRect(x, y + cellHeight, width, gridSize);

            g.setColor(bgColor);
            g.fillRect(x, y, width, cellHeight);

            if (lead) {
                g.setColor(ColorUtils.invert(bgColor));
                g.drawRect(x, y, width, cellHeight - 1);
            }

            g.setColor(fgColor);
            g.drawString(label, x + padding, y + fontOffset);

            y += height;
        }
    }

}
