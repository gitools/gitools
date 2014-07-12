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
package org.gitools.ui.app.heatmap.drawer.header;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.ui.core.HeatmapPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class HeatmapColoredLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapColoredLabelsHeader> {


    public HeatmapColoredLabelsDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapColoredLabelsHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        ColoredLabel precedingLabel = null;


        prepareDraw(g, box);
        Font previousFont = changeFont(g);

        HeatmapColoredLabelsHeader header = getHeader();
        header.reset();

        int firstIndex = firstVisibleIndex(box, clip);

        int lastIndex = lastVisibleIndex(box, clip);

        Decoration decoration = new Decoration();
        int cellWidth = header.getSize();

        int startGroupIndex = firstIndex;
        int endGroupIndex = firstIndex;

        int margin = header.getMargin();

        while (startGroupIndex <= lastIndex) {

            ColoredLabel groupLabel = header.getColoredLabel(startGroupIndex);

            while (endGroupIndex < lastIndex && groupLabel.equals(header.getColoredLabel(endGroupIndex + 1))
                    && !isHighlightedIndex(endGroupIndex + 1) && !isHighlightedIndex(endGroupIndex)) {
                endGroupIndex++;
            }

            decoration.reset();
            boolean highlighted = isHighlightedIndex(startGroupIndex);
            header.decorate(decoration, groupLabel, false);
            decoration.setRotate(true);

            int fullSize = getHeatmapDimension().getFullCellSize();
            //gridSize = 0 (no grid) if same group
            int gridSize = ((getHeatmapDimension().getCellSize() > 2 && !highlighted && !isHighlightedIndex(endGroupIndex + 1)
                    || getHeatmapDimension().getCellSize() > 2 && !groupLabel.equals(header.getColoredLabel(endGroupIndex + 1)))
                    ? getHeatmapDimension().getGridSize() : 0);

            paintCell(
                    decoration,
                    header.getBackgroundColor(),
                    gridSize,
                    margin, (startGroupIndex * fullSize),
                    cellWidth - margin,
                    (fullSize * (endGroupIndex - startGroupIndex + 1)) - gridSize,
                    g,
                    box
            );

            precedingLabel = groupLabel;
            startGroupIndex = endGroupIndex + 1;
            endGroupIndex = startGroupIndex;
        }

        g.setFont(previousFont);
    }

    @Override
    public HeatmapPosition getPosition(Point p) {
        int point = (isHorizontal() ? p.x : p.y);
        int index = getHeaderPosition(point);
        String identifier = getHeatmapDimension().getLabel(index);
        String label = getHeader().getColoredLabel(identifier).getValue();

        return (isHorizontal() ? new HeatmapPosition(getHeatmap(), -1, index, label) : new HeatmapPosition(getHeatmap(), index, -1, label));
    }

    private Font changeFont(Graphics2D g) {

        Font previousFont = g.getFont();
        Font headerFont = getHeader().getLabelFont();
        g.setFont(headerFont);
        AffineTransform fontAT = new AffineTransform();
        g.setFont(getHeader().getLabelFont().deriveFont(fontAT));

        return previousFont;
    }

    @Override
    public void drawHeaderLegend(Graphics2D g, Rectangle rect, HeatmapHeader oppositeHeatmapHeader) {


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
            List<ColoredLabel> clusters = getHeader().getClusters();

            gridSize = 1;
            oppositeMargin = decoratorHeader.getMargin();
            margin = getHeader().getMargin();
            height = (oppositeHeatmapHeader.getSize() - oppositeMargin - gridSize * annValues.size()) / annValues.size();
            width = getHeader().getSize() - margin;

            for (String v : annValues) {

                int equal = v.indexOf("=");
                if (equal != -1) {
                    v = v.substring(equal + 1).trim();
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
