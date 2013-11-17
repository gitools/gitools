package org.gitools.core.analysis.groupcomparison;

import org.gitools.core.matrix.model.*;
import org.gitools.core.stats.test.MannWhitneyWilxoxonTest;

public class GroupComparisonFunction implements IMatrixFunction<GroupComparisonResult, String> {

    private MannWhitneyWilxoxonTest test;
    private IMatrixLayer<Double> valueLayer;
    private IMatrixDimension dimension;
    private IMatrixPredicate<Double> group1Filter;
    private IMatrixPredicate<Double> group2Filter;

    public GroupComparisonFunction(MannWhitneyWilxoxonTest test, IMatrixDimension dimension, IMatrixLayer<Double> valueLayer, IMatrixPredicate<Double> group1Filter, IMatrixPredicate<Double> group2Filter) {
        this.test = test;
        this.dimension = dimension;
        this.valueLayer = valueLayer;
        this.group1Filter = group1Filter;
        this.group2Filter = group2Filter;
    }

    public GroupComparisonFunction() {

    }

    @Override
    public GroupComparisonResult apply(String identifier, IMatrixPosition position) {

        Iterable<Double> group1 = position.iterate(valueLayer, dimension).filter(group1Filter);
        Iterable<Double> group2 = position.iterate(valueLayer, dimension).filter(group2Filter);

        return test.processTest(group1, group2);
    }
}
