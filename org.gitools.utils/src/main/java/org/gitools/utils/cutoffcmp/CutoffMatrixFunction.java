package org.gitools.utils.cutoffcmp;

import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixPosition;

public class CutoffMatrixFunction extends AbstractMatrixFunction<Double, Double> {

    private CutoffCmp comparator;
    private double cutoff;

    private static Double ZERO = 0.0;
    private static Double ONE = 1.0;

    public CutoffMatrixFunction(CutoffCmp comparator, double cutoff) {
        assert comparator!=null : "The cutoff comparator cannot be NULL";

        this.comparator = comparator;
        this.cutoff = cutoff;
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {

        if (value == null) {
            return value;
        }

        return comparator.compare(value, cutoff) ? ONE : ZERO;
    }
}
