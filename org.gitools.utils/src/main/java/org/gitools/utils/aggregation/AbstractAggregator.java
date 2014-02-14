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

import org.gitools.api.analysis.IAggregator;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.primitives.Doubles.toArray;

abstract class AbstractAggregator implements IAggregator {

    private String name;

    protected AbstractAggregator(String name) {
        this.name = name;
    }

    @Override
    public Double aggregate(Iterable<Double> values) {

        Iterable<Double> noNullValues = filter(values, notNull());

        if (isEmpty(noNullValues)) {
            return null;
        }

        return aggregateNoNulls(toArray(newArrayList(noNullValues)));
    }

    @Deprecated
    protected Double aggregateNoNulls(double[] values) {
        return null;
    }

    public String toString() {
        return name;
    }

}
