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
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.model.decorator.Decoration;

import java.awt.*;

public class HeatmapDecoratorHeaderDrawer extends AbstractHeatmapHeaderDrawer<HeatmapDecoratorHeader> {

    private int firstIndex;
    private int lastIndex;

    public HeatmapDecoratorHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HeatmapDecoratorHeader header) {
        super(heatmap, heatmapDimension, header);
    }

    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        prepareDraw(g, box);

        HeatmapDecoratorHeader header = getHeader();

        int annotationWidth = getAnnotationWidth();

        firstIndex = firstVisibleIndex(box, clip);
        lastIndex = lastVisibleIndex(box, clip);

        Decoration decoration = new Decoration();

        for (int index = firstIndex; index <= lastIndex; index++) {

            int offset = getHeader().getMargin();

            // Paint border
            decoration.setBgColor(Color.WHITE);
            decoration.setValue(null);
            paintCell(decoration, index, 0, offset, g, box);

            for (String annotation : header.getAnnotationLabels()) {
                decoration.reset();
                header.decorate(decoration, index, annotation, false);
                paintCell(decoration, index, offset, annotationWidth, g, box);
                offset += annotationWidth + 1;
            }
        }
    }

    private int getAnnotationWidth() {
        int totalWidth = getHeader().getSize() - getHeader().getMargin();
        return  (totalWidth / getHeader().getAnnotationLabels().size()) - 1;
    }

    @Override
    public HeatmapPosition getPosition(Point p) {

        int point = (isHorizontal() ? p.x : p.y);
        int index = getHeaderPosition(point);
        HeatmapPosition position = (isHorizontal() ? new HeatmapPosition(-1, index) : new HeatmapPosition(index, -1));

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
        getHeader().decorate(decoration, index + firstIndex, annotation, true);
        position.headerDecoration = decoration;

        return position;
    }
}