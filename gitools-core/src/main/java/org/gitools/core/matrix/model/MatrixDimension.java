package org.gitools.core.matrix.model;

public enum MatrixDimension {

    ROWS("rows"), COLUMNS("columns");

    private String label;

    MatrixDimension(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
