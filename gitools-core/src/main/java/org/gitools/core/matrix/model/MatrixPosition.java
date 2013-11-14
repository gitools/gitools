package org.gitools.core.matrix.model;

import java.util.HashMap;
import java.util.Map;

public class MatrixPosition implements IMatrixPosition {

    private MatrixDimension[] dimensions;
    private String[] identifiers;
    private Map<MatrixDimension, Integer> positions;

    public MatrixPosition(MatrixDimension... dimensions) {
        super();

        this.dimensions = dimensions;
        this.identifiers = new String[dimensions.length];
        this.positions = new HashMap<>(dimensions.length);
    }

    @Override
    public String get(MatrixDimension dimension) {
        return identifiers[positions.get(dimension)];
    }

    @Override
    public MatrixPosition set(MatrixDimension dimension, String identifier) {

        if (identifier != null) {
            identifiers[positions.get(dimension)] = identifier;
        }

        return this;
    }

    @Override
    public String[] toVector() {
        return identifiers;
    }

    public MatrixPosition set(String... identifiers) {
        assert identifiers.length == dimensions.length : "This matrix position has " + dimensions.length + " and your identifier vector " + identifiers.length;
        this.identifiers = identifiers;
        return this;
    }

}
