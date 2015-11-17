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
package org.gitools.analysis.stats.calc;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;

public class MedianStatistic implements Statistic {

    @Override
    public String getName() {
        return "median";
    }

    @Override
    public Double calc(Iterable<Double> input) {

        List<Double> values = Lists.newArrayList(filter(input, notNull()));

        if (values.isEmpty()) {
            return null;
        }

        Collections.sort(values);
        final int middle = values.size() / 2;

        Double median = values.get(middle);
        if (values.size() % 2 == 0) {
            median = (values.get(middle - 1) + median) / 2.0;
        }

        return median;
    }

}
