package org.gitools.matrix;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.persistence.IResourceLocator;

public class TransposedMatrix implements IMatrix {

    private IMatrix matrix;

    public TransposedMatrix(IMatrix contents) {
        this.matrix = contents;
    }

    @Override
    public IMatrixDimension getRows() {
        return matrix.getColumns();
    }

    @Override
    public IMatrixDimension getColumns() {
        return matrix.getRows();
    }

    @Override
    public boolean isEmpty(int row, int column) {
        return matrix.isEmpty(column, row);
    }

    @Override
    public Object getCellValue(int row, int column, int layerIndex) {
        return matrix.getCellValue(column, row, layerIndex);
    }

    @Override
    public void setCellValue(int row, int column, int layerIndex, Object value) {
        matrix.setCellValue(column, row, layerIndex, value);
    }

    @Override
    public IMatrixLayers getLayers() {
        return matrix.getLayers();
    }

    @Override
    public void detach() {
        matrix.detach();
    }

    @Override
    public IResourceLocator getLocator() {
        return matrix.getLocator();
    }

    @Override
    public void setLocator(IResourceLocator locator) {
        matrix.setLocator(locator);
    }
}
