package org.gitools.core.matrix.sort.mutualexclusion;

import com.google.common.collect.Sets;
import org.gitools.core.matrix.model.AbstractMatrixFunction;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.utils.aggregation.IAggregator;

import java.util.Set;


public class AggregationFunction extends AbstractMatrixFunction<Double, String> {

    private IMatrixLayer<Double> layer;
    private IAggregator aggregator;
    private IMatrixDimension aggregationDimension;
    private Set<String> aggregationIdentifiers;

    public AggregationFunction(IMatrixLayer<Double> layer, IMatrixDimension aggregationDimension) {
        this(layer, layer.getAggregator(), aggregationDimension);
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers) {
        this(layer, layer.getAggregator(), aggregationDimension, aggregationIdentifiers);
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IAggregator aggregator, IMatrixDimension aggregationDimension) {
        this(layer, aggregator, aggregationDimension, Sets.newHashSet(aggregationDimension));
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IAggregator aggregator, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers) {
        super();

        this.layer = layer;
        this.aggregator = aggregator;
        this.aggregationDimension = aggregationDimension;
        this.aggregationIdentifiers = aggregationIdentifiers;
    }

    @Override
    public Double apply(String value, IMatrixPosition position) {

        return aggregator.aggregate(
                position.iterate(layer, aggregationDimension).filter(aggregationIdentifiers)
        );
    }
}
