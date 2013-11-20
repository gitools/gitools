package org.gitools.core.analysis.groupcomparison.filters;

import org.gitools.core.analysis.groupcomparison.ColumnGroup;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPredicate;

import java.util.Set;

public class GroupByLabelPredicate implements IMatrixPredicate<Double> {

    private IMatrixDimension dimension;
    private Set<String> groupIdentifiers;

    public GroupByLabelPredicate(IMatrixDimension dimension, ColumnGroup group) {
        this.dimension = dimension;
        this.groupIdentifiers = group.getColumns();
    }

    public boolean apply(Double value, IMatrixPosition position) {
        return groupIdentifiers.contains(position.get(dimension));
    }
}
