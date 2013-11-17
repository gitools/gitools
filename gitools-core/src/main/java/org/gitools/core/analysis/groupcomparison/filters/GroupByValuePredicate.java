package org.gitools.core.analysis.groupcomparison.filters;

import org.gitools.core.datafilters.BinaryCutoff;
import org.gitools.core.matrix.model.IMatrixPredicate;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixPosition;

public class GroupByValuePredicate implements IMatrixPredicate<Double> {

    private IMatrixLayer<Double> cutoffLayer;
    private BinaryCutoff binaryCutoff;
    private Double nullValue;

    public GroupByValuePredicate(IMatrixLayer<Double> cutoffLayer, BinaryCutoff binaryCutoff, Double nullValue) {
        this.cutoffLayer = cutoffLayer;
        this.binaryCutoff = binaryCutoff;
        this.nullValue = nullValue;
    }

    public boolean apply(Double value, IMatrixPosition position) {

        Double cutoffValue = position.getMatrix().get(cutoffLayer, position);

        if (cutoffValue == null) {
            cutoffValue = nullValue;
        }

        return (cutoffValue != null) && (binaryCutoff.apply(cutoffValue) == 1.0);
    }

}
