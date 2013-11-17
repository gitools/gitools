package org.gitools.core.analysis.groupcomparison;

import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPositionMapping;
import org.gitools.core.matrix.model.MatrixDimensionKey;

import java.util.HashMap;
import java.util.Map;

public class PositionMapping implements IMatrixPositionMapping {

    private Map<MatrixDimensionKey, MatrixDimensionKey> mappings = new HashMap<>();
    private Map<MatrixDimensionKey, String> fixed = new HashMap<>();

    @Override
    public void map(IMatrixPosition from, IMatrixPosition to) {

        for (MatrixDimensionKey key : to.getMatrix().getDimensions()) {

            if (fixed.containsKey(key)) {
                to.set(key, fixed.get(key));

            } else if (mappings.containsKey(key)) {
                to.set(key, from.get(mappings.get(key)));

            } else {
                to.set(from.get(key));
            }

        }
    }

    public PositionMapping map(IMatrixDimension from, IMatrixDimension to) {
        return map(from.getId(), to.getId());
    }

    public PositionMapping map(MatrixDimensionKey from, IMatrixDimension to) {
        return map(from, to.getId());
    }

    public PositionMapping map(IMatrixDimension from, MatrixDimensionKey to) {
        return map(from.getId(), to);
    }

    public PositionMapping map(MatrixDimensionKey from, MatrixDimensionKey to) {
        mappings.put(to, from);
        return this;
    }

    public PositionMapping fix(IMatrixDimension dimension, String identifier) {
        return fix(dimension.getId(), identifier);
    }

    public PositionMapping fix(MatrixDimensionKey dimensionKey, String identifier) {
        fixed.put(dimensionKey, identifier);
        return this;
    }



}
