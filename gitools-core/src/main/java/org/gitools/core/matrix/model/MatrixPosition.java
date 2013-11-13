package org.gitools.core.matrix.model;

import java.util.HashMap;
import java.util.Map;

public class MatrixPosition implements IMatrixPosition {

    private Map<String, String> positions = new HashMap<>(3);

    public MatrixPosition() {
        super();
    }

    @Override
    public String get(IMatrixDimension dimension) {
        return positions.get(dimension.getId());
    }

    public MatrixPosition set(IMatrixDimension dimension, String identifier) {
        positions.put(dimension.getId(), identifier);
        return this;
    }

}
