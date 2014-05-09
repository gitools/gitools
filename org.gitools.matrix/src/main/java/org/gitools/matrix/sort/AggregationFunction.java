/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.matrix.sort;

import com.google.common.collect.Sets;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.*;
import org.gitools.matrix.model.iterable.IdentityMatrixFunction;

import java.util.Set;


public class AggregationFunction extends AbstractMatrixFunction<Double, String> {

    private IMatrixLayer<Double> layer;
    private IAggregator aggregator;
    private IMatrixDimension aggregationDimension;
    private Set<String> aggregationIdentifiers;
    private IMatrixFunction<Double, Double> transformFunction;

    public AggregationFunction(IMatrixLayer<Double> layer, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers) {
        this(layer, layer.getAggregator(), aggregationDimension, aggregationIdentifiers, new IdentityMatrixFunction<Double>());
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IAggregator aggregator, IMatrixDimension aggregationDimension) {
        this(layer, aggregator, aggregationDimension, Sets.newHashSet(aggregationDimension), new IdentityMatrixFunction<Double>());
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IAggregator aggregator, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers, IMatrixFunction<Double, Double> transformFunction) {
        super();

        this.layer = layer;
        this.aggregator = aggregator;
        this.aggregationDimension = aggregationDimension;
        this.aggregationIdentifiers = aggregationIdentifiers;
        this.transformFunction = transformFunction;
    }

    public AggregationFunction(IMatrixLayer<Double> layer, IAggregator aggregator, IMatrixDimension aggregationDimension, IMatrixFunction<Double, Double> transformFunction) {
        this(layer, aggregator, aggregationDimension, Sets.newHashSet(aggregationDimension), transformFunction);
    }

    @Override
    public Double apply(String value, IMatrixPosition position) {

        return aggregator.aggregate(
                position.iterate(layer, aggregationDimension.subset(aggregationIdentifiers)).transform(transformFunction)
        );
    }
}
