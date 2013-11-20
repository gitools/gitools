package org.gitools.core.stats.mtc;

import org.gitools.core.matrix.filter.NullPredicate;
import org.gitools.core.matrix.model.AbstractMatrixFunction;
import org.gitools.core.matrix.model.IMatrixIterable;
import org.gitools.core.matrix.model.IMatrixPosition;

public class BonferroniMtcFunction extends AbstractMatrixFunction<Double, Double> {

    private int n = 0;

    public BonferroniMtcFunction() {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {

        n = parentIterable
                .filter(new NullPredicate<Double>())
                .count();
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        return value == null ? value : Math.min(1.0, value * n);
    }

}
