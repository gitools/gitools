package org.gitools.core.analysis.groupcomparison;

import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixFunction;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.stats.mtc.MTC;
import org.gitools.core.stats.test.results.CommonResult;

public class MultipleTestCorrectionFunction<T extends CommonResult> implements IMatrixFunction<T, T> {

    public MultipleTestCorrectionFunction(MTC mtc, IMatrixDimension dimension, String... layerIds) {
    }

    @Override
    public T apply(T value, IMatrixPosition position) {
        return null;
    }
}
