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
package org.gitools.analysis.stats.mtc;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.filter.NotNullPredicate;
import org.gitools.matrix.model.AbstractMatrixFunction;

import java.util.HashMap;
import java.util.Map;

public class BenjaminiHochbergFdrMtcFunction extends AbstractMatrixFunction<Double, Double> {

    private int n = 0;
    private IMatrixDimension iterateDimension;
    private Map<String, Integer> ranks;

    public BenjaminiHochbergFdrMtcFunction() {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {

        iterateDimension = parentIterable.getIterateDimension();
        ranks = new HashMap<>(iterateDimension.size());

        n = 0;
        int rank = 0;
        Double lastValue = -1.0;

        for (Double value : parentIterable.filter(new NotNullPredicate<Double>()).sort()) {
            n++;

            if (!lastValue.equals(value)) {
                rank++;
            }

            ranks.put(parentIterable.getPosition().get(iterateDimension), rank);
        }

    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {

        if (value == null) {
            return null;
        }

        int rank = ranks.get(position.get(iterateDimension));
        return Math.min(1.0, value * n / rank);

    }
}
