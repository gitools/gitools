package org.gitools.core.analysis.groupcomparison;

import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPositionMapping;
import org.gitools.core.matrix.model.MatrixDimensionKey;


public class NoMapping implements IMatrixPositionMapping {


    @Override
    public void map(IMatrixPosition from, IMatrixPosition to) {

        for (MatrixDimensionKey key : to.getMatrix().getDimensions()) {
            to.set(key, from.get(key));
        }

    }
}
