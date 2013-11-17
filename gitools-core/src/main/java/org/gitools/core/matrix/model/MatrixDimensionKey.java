package org.gitools.core.matrix.model;

public enum MatrixDimensionKey {

    ROWS("rows"), COLUMNS("columns");

    private String label;

    MatrixDimensionKey(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
