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

import org.apache.commons.math3.util.FastMath;
import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.filter.NotNullPredicate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BenjaminiHochbergFdrMtcFunction extends AbstractMatrixFunction<Double, Double> {

    private int n = 0;
    private Map<String, Integer> ranks;

    public BenjaminiHochbergFdrMtcFunction() {
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {

        ranks = new HashMap<>((int) parentIterable.size());

        n = 0;
        int rank = 0;
        Double lastValue = -1.0;

        IMatrixIterable<Double> sortedIterable = parentIterable.filter(new NotNullPredicate<Double>()).sort();

        for (Double value : sortedIterable) {
            n++;

            if (!lastValue.equals(value)) {
                rank++;
            }

            String hash = hashKey(sortedIterable.getPosition());

            ranks.put(hash, rank);
            lastValue = value;
        }

    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {

        if (value == null) {
            return null;
        }

        int rank = ranks.get(hashKey(position));

        double result = FastMath.min(1.0, value * n / rank);
        return result;
    }

    private String hashKey(IMatrixPosition position) {
        //TODO improve performance
        return Arrays.toString(position.toVector());
    }
}
