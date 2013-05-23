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
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.ColoredLabel;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.model.decorator.Decoration;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class HeatmapColoredLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapColoredLabelsHeader> {

    private static final double radianAngle90 = (90.0 / 180.0) * Math.PI;


    public HeatmapColoredLabelsDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapColoredLabelsHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        prepareDraw(g, box);
        Font previousFont = rotateFont(g);

        HeatmapColoredLabelsHeader header = getHeader();

        int firstIndex = firstVisibleIndex(box, clip);
        setFirstIndex(firstIndex);

        int lastIndex = lastVisibleIndex(box, clip);

        Decoration decoration = new Decoration();
        int cellWidth = header.getSize();

        int startGroupIndex = firstIndex;
        int endGroupIndex = firstIndex;

        while (startGroupIndex <= lastIndex) {

            ColoredLabel groupLabel = header.getColoredLabel(startGroupIndex);

            while (endGroupIndex < lastIndex && groupLabel.equals(header.getColoredLabel(endGroupIndex + 1))) {
                endGroupIndex++;
            }

            decoration.reset();
            header.decorate(decoration, groupLabel, false);

            int fullSize = getHeatmapDimension().getFullSize();
            int gridSize = (getHeatmapDimension().showGrid() ? getHeatmapDimension().getGridSize() : 0);

            paintCell(
                    decoration,
                    getHeatmapDimension().getGridColor(),
                    gridSize,
                    0, startGroupIndex * fullSize,
                    cellWidth,
                    (fullSize * (endGroupIndex - startGroupIndex + 1)) - gridSize,
                    g,
                    box
            );

            startGroupIndex = endGroupIndex + 1;
            endGroupIndex = startGroupIndex;
        }

        g.setFont(previousFont);
    }

    @Override
    public HeatmapPosition getPosition(Point p) {
        int point = (isHorizontal() ? p.x : p.y);
        int index = getHeaderPosition(point);
        String label = getHeader().getColoredLabel(index).getValue();

        return (isHorizontal() ? new HeatmapPosition(-1, index, label) : new HeatmapPosition(index, -1, label));
    }

    private Font rotateFont(Graphics2D g) {

        Font previousFont = g.getFont();
        Font headerFont = getHeader().getLabelFont();
        g.setFont(headerFont);
        AffineTransform fontAT = new AffineTransform();
        fontAT.rotate(radianAngle90);
        g.setFont(getHeader().getLabelFont().deriveFont(fontAT));

        return previousFont;
    }

    @Override
    public void drawHeaderLegend(@NotNull Graphics2D g, @NotNull Rectangle rect, @NotNull HeatmapHeader oppositeHeatmapHeader) {


        if (oppositeHeatmapHeader instanceof HeatmapDecoratorHeader) {

            HeatmapDecoratorHeader decoratorHeader = (HeatmapDecoratorHeader) oppositeHeatmapHeader;

            int gridSize;
            int height;
            int width;
            int margin;
            int oppositeMargin;

            int y = isHorizontal() ? rect.y : rect.y + rect.height;
            int x = rect.x;


            List<String> annValues = decoratorHeader.getAnnotationLabels();
            ColoredLabel[] clusters = getHeader().getClusters();

            gridSize = 1;
            oppositeMargin = decoratorHeader.getMargin();
            margin = getHeader().getMargin();
            height = (oppositeHeatmapHeader.getSize() - oppositeMargin - gridSize * annValues.size()) / annValues.size();
            width = getHeader().getSize() - margin;

            for (String v : annValues) {

                int equal = v.indexOf("=");
                if (equal != -1) {
                    v = v.substring(equal+1).trim();
                }

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
}
