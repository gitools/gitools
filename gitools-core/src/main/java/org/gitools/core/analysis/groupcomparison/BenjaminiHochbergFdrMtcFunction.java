package org.gitools.core.analysis.groupcomparison;

import org.gitools.core.matrix.model.AbstractMatrixFunction;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixIterable;
import org.gitools.core.matrix.model.IMatrixPosition;

import java.util.HashMap;
import java.util.Map;

public class BenjaminiHochbergFdrMtcFunction extends AbstractMatrixFunction<Double, Double> {

    private int n = 0;
    private IMatrixDimension iterateDimension;
    private Map<String, Integer> ranks;

    public BenjaminiHochbergFdrMtcFunction() {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {

        iterateDimension = parentIterable.getIterateDimension();
        ranks = new HashMap<>(iterateDimension.size());

        n = 0;
        int rank = 0;
        Double lastValue = -1.0;

        for (Double value : parentIterable.filter(new NotNullPredicate<Double>()).sort()) {
            n++;

            if (!lastValue.equals(value)) {
                rank++;
            }

            ranks.put( parentIterable.getPosition().get(iterateDimension), rank);
        }

    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {

        if (value == null) {
            return null;
        }

        int rank = ranks.get(position.get(iterateDimension));
        return Math.min(1.0, value * n / rank);

    }
}
