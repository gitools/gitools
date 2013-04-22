package org.gitools.heatmap.header;

import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.model.AbstractMatrix;
import org.gitools.matrix.model.IAnnotations;
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.IMatrixLayers;

import java.util.List;

class MatrixAdapter extends AbstractMatrix {

    private HeatmapDecoratorHeader header;

    private List<String> annotationLabels;

    public MatrixAdapter(HeatmapDecoratorHeader header) {
        this.header = header;
        this.annotationLabels = header.getHeatmapDimension().getAnnotations().getLabels();
    }

    @Override
    public Object getValue(int[] position, int layerIndex) {
        HeatmapDimension heatmapDimension = header.getHeatmapDimension();
        IAnnotations annotations = heatmapDimension.getAnnotations();
        String identifier = heatmapDimension.getLabel(position[0]);
        String value = annotations.getAnnotation(identifier, annotationLabels.get(layerIndex));

        Double number;
        try {
            number = (value == null) ? null : Double.valueOf(value);
        } catch (Exception e) {
            number = null;
        }

        return number;
    }

    public int indexOf(String label) {
        return annotationLabels.indexOf(label);
    }

    @Override
    public IMatrixDimension getRows() {
        return null;
    }

    @Override
    public IMatrixDimension getColumns() {
        return null;
    }

    @Override
    public void setValue(int[] position, int layerIndex, Object value) {
    }

    @Override
    public IMatrixLayers getLayers() {
        return null;
    }
}
