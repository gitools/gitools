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
package org.gitools.core.label;

import org.gitools.core.heatmap.HeatmapDimension;

public class AnnotationProvider implements LabelProvider {

    private final HeatmapDimension heatmapDimension;
    private final String annotationLabel;

    public AnnotationProvider(HeatmapDimension matrixDimension, String annotationLabel) {
        this.heatmapDimension = matrixDimension;
        this.annotationLabel = annotationLabel;
    }

    @Override
    public int getCount() {
        return heatmapDimension.size();
    }

    @Override
    public String getLabel(int index) {
        String identifier = heatmapDimension.getLabel(index);

        if (identifier == null) {
            return "";
        }

        String annotation = heatmapDimension.getAnnotations().getAnnotation(identifier, annotationLabel);

        if (annotation == null) {
            return "";
        }

        return annotation;
    }
}
