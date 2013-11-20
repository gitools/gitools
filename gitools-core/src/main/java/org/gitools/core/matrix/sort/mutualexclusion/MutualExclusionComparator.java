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
package org.gitools.core.matrix.sort.mutualexclusion;


import org.gitools.core.matrix.model.*;
import org.gitools.utils.aggregation.SumAbsAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MutualExclusionComparator implements Comparator<String> {

    private final Map<String, Double> aggregationCache;

    public MutualExclusionComparator(IMatrix matrix, IMatrixLayer<Double> layer, IMatrixDimension sortDimension, IMatrixPredicate<String> validIdentifiers, IMatrixDimension aggregationDimension, IProgressMonitor monitor) {

        aggregationCache = new HashMap<>(sortDimension.size());

        IMatrixPosition position = matrix.newPosition();
        Iterable<Double> aggregatedValues = position
                .iterate(sortDimension)
                .monitor(monitor, "Aggregating values")
                .filter(validIdentifiers)
                .transform(new AggregationFunction(layer, SumAbsAggregator.INSTANCE, aggregationDimension));

        for (Double value : aggregatedValues) {
            aggregationCache.put(position.get(sortDimension), value);
        }
    }

    @Override
    public int compare(String idx1, String idx2) {

        Double value1 = aggregationCache.get(idx1);
        Double value2 = aggregationCache.get(idx2);

        int res;
        int factor = SortDirection.DESCENDING.getFactor();
        if (Double.isNaN(value1) && Double.isNaN(value2)) {
            res = 0;
        } else if (Double.isNaN(value1)) {
            res = factor;
        } else if (Double.isNaN(value2)) {
            res = -factor;
        } else {
            res = value1.compareTo(value2);
        }

        return res * factor;
    }

}
