package org.gitools.heatmap.header;

import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.persistence.IResourceLocator;

import java.util.List;

class MatrixAdapter implements IMatrix {

    private HeatmapDecoratorHeader header;

    private List<String> annotationLabels;

    public MatrixAdapter(HeatmapDecoratorHeader header) {
        this.header = header;
        this.annotationLabels = header.getHeatmapDim().getAnnotations().getLabels();
    }

    @Override
    public Object getCellValue(int row, int column, int layerIndex) {
        HeatmapDimension heatmapDimension = header.getHeatmapDim();
        IAnnotations annotations = heatmapDimension.getAnnotations();
        String identifier = heatmapDimension.getLabel(row);
        String value = annotations.getAnnotation(identifier, annotationLabels.get(layerIndex));

        return (value == null) ? null : Double.valueOf(value);
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
    public boolean isEmpty(int row, int column) {
        return false;
    }

    @Override
    public void setCellValue(int row, int column, int layerIndex, Object value) {
    }

    @Override
    public IMatrixLayers getLayers() {
        return null;
    }

    @Override
    public void detach() {
    }

    @Override
    public IResourceLocator getLocator() {
        return null;
    }

    @Override
    public void setLocator(IResourceLocator locator) {
    }
}
