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
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.ui.app.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.ui.core.HeatmapPosition;

import java.awt.*;

public class HeatmapDecoratorHeaderDrawer extends AbstractHeatmapHeaderDrawer<HeatmapDecoratorHeader> {


    public HeatmapDecoratorHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapDecoratorHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        prepareDraw(g, box);

        HeatmapDecoratorHeader header = getHeader();

        int annotationWidth = getAnnotationWidth();

        int firstIndex = firstVisibleIndex(box, clip);

        int lastIndex = lastVisibleIndex(box, clip);

        Decoration decoration = new Decoration();

        g.setFont(header.getFont());

        for (int index = firstIndex; index <= lastIndex; index++) {

            String identifier = getHeatmapDimension().getLabel(index);
            int offset = getHeader().getMargin();

            // Paint border
            decoration.setBgColor(Color.WHITE);
            decoration.setValue(null);
            paintCell(decoration, index, 0, offset, g, box);

            for (String annotation : header.getAnnotationLabels()) {
                decoration.reset();
                header.decorate(decoration, identifier, annotation, false);
                paintCell(decoration, index, offset, annotationWidth, g, box);
                offset += annotationWidth + 1;
            }
        }
    }

    private int getAnnotationWidth() {
        int totalWidth = getHeader().getSize() - getHeader().getMargin();
        return (totalWidth / getHeader().getAnnotationLabels().size()) - 1;
    }

    @Override
    public HeatmapPosition getPosition(Point p) {

        int point = (isHorizontal() ? p.x : p.y);
        int index = getHeaderPosition(point);
        HeatmapPosition position = (isHorizontal() ? new HeatmapPosition(getHeatmap(), -1, index) : new HeatmapPosition(getHeatmap(), index, -1));

        int annotationWidth = getAnnotationWidth() + 1;
        int offset = (isHorizontal() ? p.y : p.x);

        int annotationIndex = offset / annotationWidth;

        if (annotationIndex < 0) {
            annotationIndex = 0;
        }

        if (annotationIndex >= getHeader().getAnnotationLabels().size()) {
            annotationIndex = getHeader().getAnnotationLabels().size() - 1;
        }

        String annotation = getHeader().getAnnotationLabels().get(annotationIndex);
        position.headerAnnotation = annotation;

        Decoration decoration = new Decoration();
        getHeader().decorate(decoration, getHeatmapDimension().getLabel(index), annotation, true);
        position.headerDecoration = decoration;

        return position;
    }
}