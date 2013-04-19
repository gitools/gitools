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
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixDimensionLabelProvider;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class HeatmapColoredLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapColoredLabelsHeader> {

    private static final double radianAngle90 = (90.0 / 180.0) * Math.PI;


    public HeatmapColoredLabelsDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapColoredLabelsHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    @Override
    public void draw(@NotNull Graphics2D g, @NotNull Rectangle box, @NotNull Rectangle clip) {

        HeatmapColoredLabelsHeader header = getHeader();
        final HeatmapDimension heatmapDimension = getHeatmapDimension();

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

        Color gridColor = heatmapDimension.getGridColor();
        int gridSize = heatmapDimension.getGridSize();

        int maxWidth = clip.width;
        int width = header.getSize();
        int height = heatmapDimension.getCellSize() + gridSize;

        width = width < maxWidth ? maxWidth : width;

        int count = heatmapDimension.size();

        int start = firstVisibleIndex(box, clip);
        int end = lastVisibleIndex(box, clip);

        start = start > 0 ? start : 0;
        end = end < count ? end : count;

        int fontOffset = ((width - fontHeight) / 2) + fm.getDescent();

        LabelProvider labelProvider = new MatrixDimensionLabelProvider(heatmapDimension);

        ColoredLabel lastCluster = null;

        int x = box.x;
        int y = box.y + start * height;
        for (int index = start; index < end; index++) {
            Color bgColor = header.getBackgroundColor();
            Color finalGridColor = gridColor;

            String label = labelProvider.getLabel(index);
            ColoredLabel cluster = header.getAssignedColoredLabel(label);
            Color clusterColor = cluster != null ? cluster.getColor() : bgColor;

            if (heatmapDimension.isHighlighted(label)) {
                bgColor = highlightingColor;
            }

            if (isSelected(index)) {
                bgColor = bgColor.darker();
                clusterColor = clusterColor.darker();
                finalGridColor = gridColor.darker();
            }

            boolean lead = !isPictureMode() && heatmapDimension.getSelectionLead() == index;

            g.setColor(bgColor);
            g.fillRect(x, y, width, height - gridSize);

            g.setColor(finalGridColor);
            g.fillRect(x, y + height - gridSize, width, gridSize);

            if (cluster != null) {
                int sepSize = 0;
                boolean clusterChanged = lastCluster == null || !cluster.equals(lastCluster);
                if (header.isSeparationGrid() && lastCluster != null && clusterChanged) {
                    sepSize = gridSize;
                }

                //int thickness = header.getThickness();
                int thickness = header.getSize() - header.getMargin();
                if (thickness < 1) {
                    thickness = 1;
                }

                g.setColor(clusterColor);
                g.fillRect(x + header.getMargin(), y + sepSize - gridSize, thickness, height - sepSize);

                // legend
                if (clusterChanged && header.isLabelVisible() && width >= fontHeight + fontDescent) {
                    if (clusterYEnd > 0 && clusterYStart >= 0) {
                        paintLegend(g, fm, fontAT, gf, labelColor, clusterYStart, clusterYEnd, legend, fontOffset, x);
                    }

                    clusterYStart = y;
                    clusterYEnd = y + height;
                    legend = cluster.getDisplayedLabel();
                } else {
                    clusterYEnd = y + height;
                }
            }

            if (lead) {
                g.setColor(ColorUtils.invert(bgColor));
                g.drawRect(x, y, width, height - gridSize - 1);
            }

            lastCluster = cluster;

            y += height;
        }

        //last legend
        if (clusterYEnd > 0 && clusterYStart >= 0) {
            paintLegend(g, fm, fontAT, gf, labelColor, clusterYStart, clusterYEnd, legend, fontOffset, x);
        }
    }

    private void paintLegend(@NotNull Graphics2D g, @NotNull FontMetrics fm, AffineTransform fontAT, @NotNull GenericFormatter gf, Color labelColor, int clusterYStart, int clusterYEnd, String legend, int fontOffset, int x) {
        String formattedLegend = gf.format(legend);
        int stringWidth = fm.stringWidth(formattedLegend);
        int clusterWidth = clusterYEnd - clusterYStart;
        if (stringWidth < clusterWidth) {
            int fontYOffset = (clusterWidth - stringWidth) / 2;
            g.setColor(getHeader().isForceLabelColor() ? labelColor : ColorUtils.invert(g.getColor()));
            g.setFont(getHeader().getLabelFont().deriveFont(fontAT));
            g.drawString(legend, x + fontOffset, clusterYStart + fontYOffset);
        }
    }

    @Override
    public void drawHeaderLegend(@NotNull Graphics2D g, @NotNull Rectangle rect, @NotNull HeatmapHeader oppositeHeatmapHeader) {
        int gridSize;
        int height;
        int width;
        int margin;
        int oppositeMargin;

        int y = isHorizontal() ? rect.y : rect.y + rect.height;
        int x = rect.x;


        String[] annValues = oppositeHeatmapHeader.getAnnotationValues(isHorizontal());
        ColoredLabel[] clusters = getHeader().getClusters();

        gridSize = 1;
        oppositeMargin = oppositeHeatmapHeader.getMargin();
        margin = getHeader().getMargin();
        height = (oppositeHeatmapHeader.getSize() - oppositeMargin - gridSize * annValues.length) / annValues.length;
        width = getHeader().getSize() - margin;

        for (String v : annValues) {
            for (ColoredLabel cl : clusters) {
                if (cl.getValue().equals(v)) {
                    // paint
                    g.setColor(cl.getColor());

                    if (isHorizontal()) {

                        g.fillRect(x + oppositeMargin, y, height, width);
                        x += gridSize + height;
                    } else {
                        y -= height;
                        g.fillRect(x, y - oppositeMargin, width, height);
                        y -= gridSize;
                    }
                }
            }
        }
    }
}
