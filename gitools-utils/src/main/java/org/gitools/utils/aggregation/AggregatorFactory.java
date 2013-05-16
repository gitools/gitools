/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.aggregation;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AggregatorFactory {

    private static final List<IAggregator> aggregators = new ArrayList<IAggregator>();

    private static final Map<String, IAggregator> aggregatorsMap = new HashMap<String, IAggregator>();

    static {
        put(MultAggregator.INSTANCE);
        put(SumAggregator.INSTANCE);
        put(MeanAggregator.INSTANCE);
        put(LogSumAggregator.INSTANCE);
        put(SumAbsAggregator.INSTANCE);
        put(StdDevAggregator.INSTANCE);
        put(VarianceAggregator.INSTANCE);
        put(MinAggregator.INSTANCE);
        put(MaxAggregator.INSTANCE);
        put(NonZeroPercentageAggregator.INSTANCE);

    }

    private static void put(@NotNull IAggregator aggregator) {
        aggregators.add(aggregator);
        aggregatorsMap.put(aggregator.toString(), aggregator);
    }

    public static IAggregator create(String name) {
        return aggregatorsMap.get(name);
    }

    public static Collection<IAggregator> getAggregators() {
        return Collections.unmodifiableCollection(aggregators);
    }

    @NotNull
    public static IAggregator[] getAggregatorsArray() {
        final IAggregator[] aggregatorsArray = new IAggregator[aggregators.size()];
        aggregators.toArray(aggregatorsArray);
        return aggregatorsArray;
    }
}
