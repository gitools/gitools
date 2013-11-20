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

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import org.gitools.api.analysis.IAggregator;

abstract class AbstractAggregator implements IAggregator {

    @Override
    public Double aggregate(Iterable<Double> data) {
        return aggregate(
                Doubles.toArray(
                        Lists.newArrayList(
                                Iterables.filter(data, Predicates.notNull())
                        )
                )
        );
    }

    double aggregate(double[] data, DoubleDoubleFunction reduceFunc, DoubleFunction mapFunc) {

        // Look for the first non-NaN value
        int first = 0;
        while (Double.isNaN(data[first])) {
            first++;

            if (first == data.length) {
                return Double.NaN;
            }
        }

        // Look for the second non-Nan value
        int second = first;
        do {
            second++;

            // If there is only one non-Nan return this value mapped
            if (second == data.length) {
                return mapFunc.apply(data[first]);
            }

        } while (Double.isNaN(data[second]));

        // Aggregate first two values
        double total = reduceFunc.apply(mapFunc.apply(data[first]), mapFunc.apply(data[second]));

        // Aggregate rhe remaining values
        for (int i = second + 1; i < data.length; i++) {

            // Skip NaN values
            if (Double.isNaN(data[i])) {
                continue;
            }

            total = reduceFunc.apply(total, mapFunc.apply(data[i]));
        }

        return total;
    }

    /**
     * A simple mapping functions that always returns the same input value
     */
    private static final DoubleFunction NO_MAPPING = new DoubleFunction() {
        @Override
        public double apply(double v) {
            return v;
        }
    };

    double aggregate(double[] data, DoubleDoubleFunction reduceFunc) {
        return aggregate(data, reduceFunc, NO_MAPPING);
    }


}
