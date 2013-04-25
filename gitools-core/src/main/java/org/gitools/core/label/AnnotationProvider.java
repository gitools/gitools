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
