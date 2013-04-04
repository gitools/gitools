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

import cern.jet.math.Functions;
import org.jetbrains.annotations.NotNull;

import static cern.jet.stat.Descriptive.sampleVariance;
import static cern.jet.stat.Descriptive.standardDeviation;

/**
 * Sum of logarithms
 */
public class StdDevAggregator extends AbstractAggregator
{

    public final static IAggregator INSTANCE = new StdDevAggregator();

    private StdDevAggregator()
    {
    }

    @Override
    public double aggregate(@NotNull double[] data)
    {
        int size = data.length;
        double sum = aggregate(data, Functions.plus, 0);
        double sumOfSquares = aggregate(data, Functions.plus, Functions.square, 0);
        double variance = sampleVariance(size, sum, sumOfSquares);
        return standardDeviation(variance);
    }

    @NotNull
    @Override
    public String toString()
    {
        return "Standard deviation";
    }
}
